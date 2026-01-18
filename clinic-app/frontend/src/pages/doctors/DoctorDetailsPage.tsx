import { useState } from "react";
import { getDoctor } from "../../api/doctors";
import type { DoctorDetailsDTO } from "../../types/doctor.ts";
import { deleteShift } from "../../api/shifts";

export default function DoctorDetailsPage() {
  const [id, setId] = useState<string>("");
  const [doctor, setDoctor] = useState<DoctorDetailsDTO | null>(null);
  const [msg, setMsg] = useState<string>("");

  const load = async () => {
    setMsg("");
    setDoctor(null);

    const parsed = Number(id);
    try {
      const data = await getDoctor(parsed);
      setDoctor(data);
    } catch (e) {
      setMsg(`Error: ${e instanceof Error ? e.message : "unknown"}`);
    }
  };

  const handleDeleteShift = async (shiftId: number) => {
    if (!confirm("Are you sure you want to delete this shift?")) {
      return;
    }

    try {
      await deleteShift(shiftId);
      await load();
    } catch (e) {
      setMsg(
        `Error deleting shift: ${e instanceof Error ? e.message : "unknown"}`,
      );
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleString("en-UK", {
      weekday: "short",
      year: "numeric",
      month: "numeric",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  return (
    <div>
      <h2>Doctor details</h2>

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
        />
        <button onClick={() => void load()}>Find</button>
      </div>

      {msg && <p style={{ marginTop: 12, color: "crimson" }}>{msg}</p>}

      {doctor && (
        <div
          style={{
            marginTop: 12,
            padding: 12,
            border: "1px solid #ddd",
            borderRadius: 12,
          }}
        >
          <h3 style={{ marginTop: 0 }}>
            {doctor.firstName} {doctor.lastName}
          </h3>
          <p>
            <strong>specialization:</strong> {doctor.specialization}
          </p>
          <p>
            <strong>addres:</strong> {doctor.address}
          </p>

          <div>
            <h4>Shifts plan:</h4>

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
                    Office
                  </th>
                  <th
                    style={{ padding: "8px", borderBottom: "2px solid #ddd" }}
                  >
                    From
                  </th>
                  <th
                    style={{ padding: "8px", borderBottom: "2px solid #ddd" }}
                  >
                    To
                  </th>
                  <th
                    style={{ padding: "8px", borderBottom: "2px solid #ddd" }}
                  >
                    Action
                  </th>
                </tr>
              </thead>
              <tbody>
                {doctor.shifts && doctor.shifts.length > 0 ? (
                  doctor.shifts.map((shift, index) => (
                    <tr key={index} style={{ borderBottom: "1px solid #eee" }}>
                      <td style={{ padding: "8px" }}>
                        <strong>{shift.officeName}</strong>
                      </td>
                      <td style={{ padding: "8px" }}>
                        {formatDate(shift.start)}
                      </td>
                      <td style={{ padding: "8px" }}>
                        {formatDate(shift.end)}
                      </td>
                      <td style={{ padding: "8px" }}>
                        <button
                          onClick={() => handleDeleteShift(shift.id)}
                          style={{
                            backgroundColor: "#ff4d4d",
                            color: "white",
                            border: "none",
                            padding: "5px 10px",
                            cursor: "pointer",
                            borderRadius: "4px",
                          }}
                        >
                          Delete
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
                      No shifts assigned.
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
