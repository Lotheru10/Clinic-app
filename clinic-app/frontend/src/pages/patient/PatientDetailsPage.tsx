import { useState } from "react";
import { getPatient } from "../../api/patients";
import {} from "../../api/appointment";
import type { PatientDetailsDTO } from "../../types/patient.ts";

export default function PatientDetailsPage() {
  const [id, setId] = useState<string>("");
  const [patient, setPatient] = useState<PatientDetailsDTO | null>(null);
  const [msg, setMsg] = useState<string>("");

  const load = async () => {
    setMsg("");
    setPatient(null);

    const parsed = Number(id);
    try {
      const data = await getPatient(parsed);
      setPatient(data);
    } catch (e) {
      setMsg(`Error: ${e instanceof Error ? e.message : "unknown"}`);
    }
  };

  //const handleCancelAppointment = async (appointmentId: number) => {
  //      if (!confirm("Are you sure you want to cancel this appointment?")) return;
  //
  //      try {
  //          await deleteAppointment(appointmentId);
  //          // Odświeżamy dane pacjenta po usunięciu
  //          await load();
  //      } catch (e) {
  //          setMsg(`Error cancelling appointment: ${e instanceof Error ? e.message : "unknown"}`);
  //      }
  //  };

  const formatDate = (dateString: string) => {
    if (!dateString) return "-";
    return new Date(dateString).toLocaleString("en-UK", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  return (
    <div>
      <h2>Patient details</h2>

      <div
        style={{
          display: "flex",
          gap: 10,
          alignItems: "center",
          flexWrap: "wrap",
        }}
      >
        <input
          value={id}
          onChange={(e) => setId(e.target.value)}
          style={{ width: 160 }}
          placeholder="Patient ID"
        />
        <button onClick={() => void load()}>Find</button>
      </div>

      {msg && <p style={{ marginTop: 12, color: "crimson" }}>{msg}</p>}

      {patient && (
        <div
          style={{
            marginTop: 12,
            padding: 12,
            border: "1px solid #ddd",
            borderRadius: 12,
          }}
        >
          <h3 style={{ marginTop: 0 }}>
            {patient.firstName} {patient.lastName}
          </h3>
          <p>
            <strong>Address:</strong> {patient.address}
          </p>

          {/* SEKCJA WIZYT */}
          <div style={{ marginTop: 20 }}>
            <h4>Appointments History:</h4>

            <table
              style={{
                width: "100%",
                borderCollapse: "collapse",
                fontSize: "0.9rem",
              }}
            >
              <thead>
                <tr style={{ background: "#f9f9f9", textAlign: "left" }}>
                  <th
                    style={{ padding: "8px", borderBottom: "2px solid #ddd" }}
                  >
                    Date
                  </th>
                  <th
                    style={{ padding: "8px", borderBottom: "2px solid #ddd" }}
                  >
                    Doctor
                  </th>
                  <th
                    style={{ padding: "8px", borderBottom: "2px solid #ddd" }}
                  >
                    Office
                  </th>
                  <th
                    style={{ padding: "8px", borderBottom: "2px solid #ddd" }}
                  >
                    Action
                  </th>
                </tr>
              </thead>
              <tbody>
                {patient.appointments && patient.appointments.length > 0 ? (
                  patient.appointments.map((appt) => (
                    <tr
                      key={appt.id}
                      style={{ borderBottom: "1px solid #eee" }}
                    >
                      <td style={{ padding: "8px" }}>
                        {formatDate(appt.start)}
                      </td>
                      <td style={{ padding: "8px" }}>{appt.doctorName}</td>
                      <td style={{ padding: "8px" }}>{appt.officeName}</td>
                      <td style={{ padding: "8px" }}>
                        <button
                          //onClick={() => handleCancelAppointment(appt.id)}
                          style={{
                            backgroundColor: "#ff4d4d",
                            color: "white",
                            border: "none",
                            padding: "5px 10px",
                            cursor: "pointer",
                            borderRadius: "4px",
                          }}
                        >
                          Cancel
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td
                      colSpan={4}
                      style={{
                        padding: "12px",
                        textAlign: "center",
                        color: "#888",
                      }}
                    >
                      No appointments found.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}
