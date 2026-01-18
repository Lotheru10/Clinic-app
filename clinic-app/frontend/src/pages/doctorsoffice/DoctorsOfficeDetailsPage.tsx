import { useState } from "react";
import { getDoctorsOffice } from "../../api/doctorsoffices.ts";
import type { DoctorsOfficeDetailsDTO } from "../../types/doctorsoffice.ts";
import { deleteShift } from "../../api/shifts.ts";

export default function DoctorsOfficeDetailsPage() {
  const [id, setId] = useState<string>("");
  const [doctorsOffice, setDoctorsOffice] =
    useState<DoctorsOfficeDetailsDTO | null>(null);
  const [msg, setMsg] = useState<string>("");

  const load = async () => {
    setMsg("");
    setDoctorsOffice(null);

    const parsed = Number(id);
    try {
      const data = await getDoctorsOffice(parsed);
      setDoctorsOffice(data);
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
    if (!dateString) return "";
    return new Date(dateString).toLocaleString("pl-PL", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  return (
    <div>
      <h2>Doctor's office details</h2>

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

      {doctorsOffice && (
        <div
          style={{
            marginTop: 12,
            padding: 12,
            border: "1px solid #ddd",
            borderRadius: 12,
          }}
        >
          <h3 style={{ marginTop: 0 }}>{doctorsOffice.roomNumber}</h3>
          <p>
            <strong>description:</strong> {doctorsOffice.roomDescription}
          </p>

          <div>
            <h4>Harmonogram dyżurów:</h4>

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
                    Doctor
                  </th>
                  <th
                    style={{ padding: "8px", borderBottom: "2px solid #ddd" }}
                  >
                    Start
                  </th>
                  <th
                    style={{ padding: "8px", borderBottom: "2px solid #ddd" }}
                  >
                    End
                  </th>
                  <th
                    style={{ padding: "8px", borderBottom: "2px solid #ddd" }}
                  >
                    Action
                  </th>
                </tr>
              </thead>
              <tbody>
                {doctorsOffice.shifts && doctorsOffice.shifts.length > 0 ? (
                  doctorsOffice.shifts.map((shift, index) => (
                    <tr key={index} style={{ borderBottom: "1px solid #eee" }}>
                      <td style={{ padding: "8px" }}>
                        <strong>{shift.doctorName}</strong>
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
