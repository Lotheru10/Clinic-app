import { useEffect, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";

import { getPatients } from "../../api/patients.ts";
import { getDoctors } from "../../api/doctors.ts";
import { getAvailableSlots, addAppointment } from "../../api/appointment.ts";
import type { PatientDTO } from "../../types/patient.ts";
import type { DoctorDTO } from "../../types/doctor.ts";
import type { AvailableAppointmentSlotDTO } from "../../types/appointment.ts";
import "../../css/calendar.css";

// --- UTILS ---

const toLocalISO = (date: Date): string => {
  const offset = date.getTimezoneOffset() * 60000;
  return new Date(date.getTime() - offset).toISOString().slice(0, 10);
};

const getLocalISOString = (date: Date): string => {
  const offset = date.getTimezoneOffset() * 60000;
  return new Date(date.getTime() - offset).toISOString().slice(0, 19);
};

const formatDateKey = (input: Date | number[] | string): string => {
  if (Array.isArray(input)) {
    const [y, m, d] = input;
    return `${y}-${String(m).padStart(2, "0")}-${String(d).padStart(2, "0")}`;
  }
  if (input instanceof Date) return input.toLocaleDateString("en-CA");
  if (typeof input === "string") return input.split("T")[0];
  return "";
};

const parseBackendDate = (input: string | number[]): Date => {
  if (Array.isArray(input)) {
    return new Date(input[0], input[1] - 1, input[2], input[3], input[4]);
  }
  return new Date(input);
};

// --- COMPONENT ---

export default function AppointmentCreatePage() {
  const navigate = useNavigate();

  // State
  const [patients, setPatients] = useState<PatientDTO[]>([]);
  const [doctors, setDoctors] = useState<DoctorDTO[]>([]);
  const [selectedPatient, setSelectedPatient] = useState<PatientDTO | null>(
    null,
  );
  const [selectedDoctor, setSelectedDoctor] = useState<DoctorDTO | null>(null);

  // Calendar State
  const [date, setDate] = useState<Date | null>(null);
  const [viewDate, setViewDate] = useState<Date>(new Date());
  const [availableDays, setAvailableDays] = useState<Set<string>>(new Set());
  const [daySlots, setDaySlots] = useState<AvailableAppointmentSlotDTO[]>([]);

  // UI State
  const [loadingData, setLoadingData] = useState(true);
  const [loadingSlots, setLoadingSlots] = useState(false);
  const [globalError, setGlobalError] = useState("");

  // Initial Data Load
  useEffect(() => {
    const loadData = async () => {
      try {
        const [p, d] = await Promise.all([getPatients(), getDoctors()]);
        setPatients(p);
        setDoctors(d);
      } catch (e) {
        setGlobalError("Failed to load initial data.");
      } finally {
        setLoadingData(false);
      }
    };
    void loadData();
  }, []);

  // Fetch Month Availability (Highlighting)
  useEffect(() => {
    if (!selectedDoctor) return;

    const fetchMonth = async () => {
      const year = viewDate.getFullYear();
      const month = viewDate.getMonth() + 1;
      const lastDay = new Date(year, month, 0).getDate();

      const monthStart = `${year}-${String(month).padStart(2, "0")}-01T00:00:00`;
      const monthEnd = `${year}-${String(month).padStart(2, "0")}-${lastDay}T23:59:59`;

      const now = new Date();
      const safeNow = new Date(now.getTime() + 5 * 60000); // 5 min buffer

      if (new Date(monthEnd) < safeNow) {
        setAvailableDays(new Set());
        return;
      }

      let queryStart = monthStart;
      if (new Date(monthStart) < safeNow) {
        queryStart = getLocalISOString(safeNow);
      }

      if (queryStart > monthEnd) return;

      try {
        const slots = await getAvailableSlots(
          selectedDoctor.id,
          queryStart,
          monthEnd,
        );
        const days = new Set(
          slots.map((s) => formatDateKey(s.start)).filter(Boolean),
        );
        setAvailableDays(days);
      } catch (e) {
        console.warn("Month availability fetch failed", e);
      }
    };

    void fetchMonth();
  }, [selectedDoctor, viewDate]);

  // Fetch Day Slots
  useEffect(() => {
    if (!selectedDoctor || !date) return;

    const fetchDay = async () => {
      setLoadingSlots(true);
      setDaySlots([]);

      try {
        const dateKey = formatDateKey(date);
        let startIso = `${dateKey}T00:00:00`;
        const endIso = `${dateKey}T23:59:59`;

        const now = new Date();
        const selectedStart = new Date(startIso);
        const todayStart = new Date(
          now.getFullYear(),
          now.getMonth(),
          now.getDate(),
        );

        if (selectedStart < todayStart) return;

        if (selectedStart.getTime() === todayStart.getTime()) {
          const safeNow = new Date(now.getTime() + 5 * 60000);
          startIso = getLocalISOString(safeNow);
        }

        if (startIso > endIso) return;

        const data = await getAvailableSlots(
          selectedDoctor.id,
          startIso,
          endIso,
        );
        setDaySlots(data);
      } catch (e) {
        console.warn("Day slots fetch failed", e);
      } finally {
        setLoadingSlots(false);
      }
    };

    void fetchDay();
  }, [selectedDoctor, date]);

  // Handlers
  const handleBook = async (slot: AvailableAppointmentSlotDTO) => {
    if (!selectedPatient) return;
    const dateStr = parseBackendDate(slot.start).toLocaleString();

    if (
      !confirm(
        `Book appointment for ${selectedPatient.lastName} on ${dateStr}?`,
      )
    )
      return;

    try {
      await addAppointment({
        patientId: selectedPatient.id,
        shiftId: slot.shiftId,
        start: slot.start,
      });
      alert("Appointment booked successfully!");
      navigate("/appointments");
    } catch (e) {
      alert(`Error: ${e instanceof Error ? e.message : "unknown"}`);
    }
  };

  const tileClassName = useCallback(
    ({ date, view }: { date: Date; view: string }) => {
      if (view === "month" && availableDays.has(formatDateKey(date))) {
        return "highlight-green";
      }
      return null;
    },
    [availableDays],
  );

  const resetSelection = (type: "patient" | "doctor") => {
    setDate(null);
    setDaySlots([]);
    if (type === "patient") {
      setSelectedPatient(null);
      setSelectedDoctor(null);
    } else {
      setSelectedDoctor(null);
    }
  };

  if (loadingData) return <p>Loading data...</p>;
  if (globalError) return <p style={styles.errorText}>{globalError}</p>;

  return (
    <div style={styles.container}>
      <h2>New Appointment</h2>

      {/* STEP 1: PATIENT */}
      {!selectedPatient ? (
        <div>
          <h3>Step 1: Select a Patient</h3>
          <table style={styles.table}>
            <thead style={styles.thead}>
              <tr>
                <th style={styles.th}>Name</th>
                <th style={styles.th}>Action</th>
              </tr>
            </thead>
            <tbody>
              {patients.map((p) => (
                <tr key={p.id} style={styles.tr}>
                  <td style={styles.td}>
                    {p.firstName} {p.lastName}
                  </td>
                  <td style={styles.td}>
                    <button onClick={() => setSelectedPatient(p)}>
                      Select
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <div style={styles.summary}>
          <span>
            Patient:{" "}
            <strong>
              {selectedPatient.firstName} {selectedPatient.lastName}
            </strong>
          </span>
          <button
            onClick={() => resetSelection("patient")}
            style={styles.smallBtn}
          >
            Change
          </button>
        </div>
      )}

      {/* STEP 2: DOCTOR */}
      {selectedPatient && !selectedDoctor && (
        <div>
          <h3>Step 2: Select a Doctor</h3>
          <table style={styles.table}>
            <thead style={styles.theadBlue}>
              <tr>
                <th style={styles.th}>Name</th>
                <th style={styles.th}>Specialization</th>
                <th style={styles.th}>Action</th>
              </tr>
            </thead>
            <tbody>
              {doctors.map((d) => (
                <tr key={d.id} style={styles.tr}>
                  <td style={styles.td}>
                    {d.firstName} {d.lastName}
                  </td>
                  <td style={styles.td}>{d.specialization}</td>
                  <td style={styles.td}>
                    <button onClick={() => setSelectedDoctor(d)}>Select</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {selectedPatient && selectedDoctor && (
        <div style={styles.summary}>
          <span>
            Doctor:{" "}
            <strong>
              {selectedDoctor.firstName} {selectedDoctor.lastName}
            </strong>
          </span>
          <button
            onClick={() => resetSelection("doctor")}
            style={styles.smallBtn}
          >
            Change
          </button>
        </div>
      )}

      {/* STEP 3: CALENDAR */}
      {selectedPatient && selectedDoctor && (
        <div style={styles.calendarContainer}>
          <h3>Step 3: Select Date & Time</h3>

          <Calendar
              className="my-calendar"
            onChange={(val) => setDate(val as Date)}
            value={date}
            onActiveStartDateChange={({ activeStartDate }) =>
              setViewDate(activeStartDate || new Date())
            }
            tileClassName={tileClassName}
          />

          <div style={{ marginTop: 20, width: "100%" }}>
            {loadingSlots && (
              <p style={{ textAlign: "center" }}>Checking hours...</p>
            )}

            {!loadingSlots && date && daySlots.length === 0 && (
              <div style={styles.noSlots}>
                No available slots for <strong>{toLocalISO(date)}</strong>.
              </div>
            )}

            {daySlots.length > 0 && (
              <div>
                <h4 style={{ textAlign: "center" }}>
                  Available Hours for {toLocalISO(date!)}:
                </h4>
                <div style={styles.grid}>
                  {daySlots.map((slot, idx) => {
                    const timeLabel = parseBackendDate(
                      slot.start,
                    ).toLocaleTimeString([], {
                      hour: "2-digit",
                      minute: "2-digit",
                    });
                    return (
                      <button
                        key={idx}
                        onClick={() => handleBook(slot)}
                        style={styles.slotBtn}
                        onMouseOver={(e) =>
                          (e.currentTarget.style.backgroundColor = "#45a049")
                        }
                        onMouseOut={(e) =>
                          (e.currentTarget.style.backgroundColor = "#4CAF50")
                        }
                      >
                        {timeLabel}
                      </button>
                    );
                  })}
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

// --- STYLES ---

const styles = {
  container: { padding: "20px", maxWidth: "800px", margin: "0 auto" },
  errorText: { color: "crimson" },
  table: {
    width: "100%",
    borderCollapse: "collapse" as const,
    marginBottom: 20,
  },
  thead: { background: "#594242" },
  theadBlue: { background: "#90aaaf" },
  th: { padding: 8, textAlign: "left" as const },
  tr: { borderBottom: "1px solid #ddd" },
  td: { padding: 8 },
  summary: {
    marginBottom: 10,
    padding: "10px 15px",
    border: "1px solid #ccc",
    borderRadius: 8,
    background: "#876b6b",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  smallBtn: { fontSize: "0.8rem", padding: "4px 8px", cursor: "pointer" },
  calendarContainer: {
    marginTop: 20,
    display: "flex",
    flexDirection: "column" as const,
    alignItems: "center",
  },
  noSlots: {
    textAlign: "center" as const,
    padding: 20,
    background: "#f8d7da",
    color: "#721c24",
    borderRadius: 8,
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(100px, 1fr))",
    gap: 10,
  },
  slotBtn: {
    padding: "10px",
    backgroundColor: "#4CAF50",
    color: "white",
    border: "none",
    borderRadius: "5px",
    cursor: "pointer",
    fontSize: "1.1rem",
    transition: "background 0.3s",
  },

};
