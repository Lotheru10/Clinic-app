import { useEffect, useState } from "react";
import { getPatients } from "../../api/patients.ts";
import { getDoctors } from "../../api/doctors.ts";
import type { PatientDTO } from "../../types/patient.ts";
import type { DoctorDTO } from "../../types/doctor.ts";

export default function AppointmentCreatePage() {
  const [patients, setPatients] = useState<PatientDTO[]>([]);
  const [doctors, setDoctors] = useState<DoctorDTO[]>([]);

  const [selectedPatient, setSelectedPatient] = useState<PatientDTO | null>(
    null,
  );
  const [selectedDoctor, setSelectedDoctor] = useState<DoctorDTO | null>(null);

  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>("");

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const [patientsData, doctorsData] = await Promise.all([
          getPatients(),
          getDoctors(),
        ]);
        setPatients(patientsData);
        setDoctors(doctorsData);
      } catch (e) {
        setError("Failed to load data.");
      } finally {
        setLoading(false);
      }
    };

    void fetchData();
  }, []);

  const handleResetPatient = () => {
    setSelectedPatient(null);
    setSelectedDoctor(null);
  };

  if (loading) return <p>Loading data...</p>;
  if (error) return <p style={{ color: "crimson" }}>{error}</p>;

  return (
    <div>
      <h2>New Appointment</h2>

      {!selectedPatient ? (
        <div>
          <h3>Select a Patient</h3>
          <table
            style={{
              width: "100%",
              borderCollapse: "collapse",
              marginBottom: 20,
            }}
          >
            <thead>
              <tr style={{ background: "#eee", textAlign: "left" }}>
                <th style={{ padding: 8 }}>ID</th>
                <th style={{ padding: 8 }}>First Name</th>
                <th style={{ padding: 8 }}>Last Name</th>
                <th style={{ padding: 8 }}>Action</th>
              </tr>
            </thead>
            <tbody>
              {patients.map((patient) => (
                <tr key={patient.id} style={{ borderBottom: "1px solid #ddd" }}>
                  <td style={{ padding: 8 }}>{patient.id}</td>
                  <td style={{ padding: 8 }}>{patient.firstName}</td>
                  <td style={{ padding: 8 }}>{patient.lastName}</td>
                  <td style={{ padding: 8 }}>
                    <button onClick={() => setSelectedPatient(patient)}>
                      Select
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <div
          style={{
            marginBottom: 20,
            padding: 15,
            border: "1px solid #ccc",
            borderRadius: 8,
            background: "#f9f9f9",
          }}
        >
          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
            }}
          >
            <span>
              Selected Patient:{" "}
              <strong>
                {selectedPatient.firstName} {selectedPatient.lastName}
              </strong>
            </span>
            <button onClick={handleResetPatient} style={{ fontSize: "0.8rem" }}>
              Change Patient
            </button>
          </div>
        </div>
      )}

      {selectedPatient && (
        <div>
          <h3>Select a Doctor</h3>
          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr style={{ background: "#e0f7fa", textAlign: "left" }}>
                <th style={{ padding: 8 }}>ID</th>
                <th style={{ padding: 8 }}>Name</th>
                <th style={{ padding: 8 }}>Specialization</th>
                <th style={{ padding: 8 }}>Action</th>
              </tr>
            </thead>
            <tbody>
              {doctors.map((doctor) => (
                <tr key={doctor.id} style={{ borderBottom: "1px solid #ddd" }}>
                  <td style={{ padding: 8 }}>{doctor.id}</td>
                  <td style={{ padding: 8 }}>
                    {doctor.firstName} {doctor.lastName}
                  </td>
                  <td style={{ padding: 8 }}>{doctor.specialization}</td>
                  <td style={{ padding: 8 }}>
                    <button
                      onClick={() => setSelectedDoctor(doctor)}
                      style={{
                        backgroundColor:
                          selectedDoctor?.id === doctor.id ? "#4CAF50" : "",
                      }}
                    >
                      {selectedDoctor?.id === doctor.id ? "Selected" : "Select"}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {selectedDoctor && (
            <div style={{ marginTop: 20 }}>
              <p>
                Wybrano lekarza: <b>{selectedDoctor.lastName}</b>. Tutaj w
                przyszłości pojawi się kalendarz.
              </p>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
