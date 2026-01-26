import { useMemo, useState } from "react";
import { getDoctor } from "../../api/doctors";
import { deleteShift } from "../../api/shifts";

import type { DoctorDetailsDTO } from "../../types/doctor";
import type { OpinionDTO } from "../../types/opinion";

function Stars({ value }: { value: number }) {
  const full = Math.round(value); // prosto: zaokrąglamy do całych
  return (
      <span style={{ whiteSpace: "nowrap" }}>
      {"★".repeat(full)}
        {"☆".repeat(5 - full)}
    </span>
  );
}

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
    if (!confirm("Are you sure you want to delete this shift?")) return;

    try {
      await deleteShift(shiftId);
      await load();
    } catch (e) {
      setMsg(`Error deleting shift: ${e instanceof Error ? e.message : "unknown"}`);
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleString("en-GB", {
      weekday: "short",
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const avgRate = useMemo(() => {
    const ops = doctor?.opinions ?? [];
    if (ops.length === 0) return 0;
    const sum = ops.reduce((acc, o) => acc + (o.rate ?? 0), 0);
    return sum / ops.length;
  }, [doctor]);

  return (
      <div>
        <h2>Doctor details</h2>

        <div style={{ display: "flex", gap: 10, alignItems: "center", flexWrap: "wrap" }}>
          <input value={id} onChange={(e) => setId(e.target.value)} style={{ width: 160 }} />
          <button onClick={() => void load()}>Find</button>
        </div>

        {msg && <p style={{ marginTop: 12, color: "crimson" }}>{msg}</p>}

        {doctor && (
            <div style={{ marginTop: 12, padding: 12, border: "1px solid #ddd", borderRadius: 12 }}>
              <h3 style={{ marginTop: 0 }}>
                {doctor.firstName} {doctor.lastName}
              </h3>

              <p>
                <strong>specialization:</strong> {doctor.specialization}
              </p>

              <p>
                <strong>address:</strong> {doctor.address}
              </p>

              {/* OPINIONS */}
              <div style={{ marginTop: 20 }}>
                <h4 style={{ marginBottom: 8 }}>Opinions:</h4>

                {doctor.opinions && doctor.opinions.length > 0 ? (
                    <div
                        style={{
                          border: "1px solid #eee",
                          borderRadius: 12,
                          padding: 12,
                        }}
                    >
                      <div style={{ display: "flex", alignItems: "center", gap: 10, marginBottom: 10 }}>
                        <strong>Average:</strong>
                        <span>{avgRate.toFixed(2)}/5</span>
                        <Stars value={avgRate} />
                        <span style={{ color: "#666" }}>({doctor.opinions.length})</span>
                      </div>

                      <table style={{ width: "100%", borderCollapse: "collapse", fontSize: "0.9rem" }}>
                        <thead>
                        <tr style={{ textAlign: "left" }}>
                          <th style={{ padding: "8px", borderBottom: "2px solid #ddd" }}>Rate</th>
                          <th style={{ padding: "8px", borderBottom: "2px solid #ddd" }}>Patient</th>
                          <th style={{ padding: "8px", borderBottom: "2px solid #ddd" }}>Date</th>
                          <th style={{ padding: "8px", borderBottom: "2px solid #ddd" }}>Message</th>
                        </tr>
                        </thead>

                        <tbody>
                        {doctor.opinions
                            .slice()
                            .sort(
                                (a: OpinionDTO, b: OpinionDTO) =>
                                    new Date(b.date).getTime() - new Date(a.date).getTime(),
                            )
                            .map((op, idx) => (
                                <tr key={idx} style={{ borderBottom: "1px solid #eee" }}>
                                  <td style={{ padding: "8px" }}>
                            <span title={`${op.rate}/5`}>
                              {"★".repeat(op.rate)}
                              {"☆".repeat(5 - op.rate)}
                            </span>
                                  </td>
                                  <td style={{ padding: "8px" }}>{op.patientName ?? "-"}</td>
                                  <td style={{ padding: "8px" }}>{op.date ? formatDate(op.date) : "-"}</td>
                                  <td style={{ padding: "8px" }}>{op.message || "-"}</td>
                                </tr>
                            ))}
                        </tbody>
                      </table>
                    </div>
                ) : (
                    <div style={{ padding: 12, color: "#888", textAlign: "center" }}>
                      No opinions yet.
                    </div>
                )}
              </div>

              {/* SHIFTS */}
              <div style={{ marginTop: 20 }}>
                <h4>Shifts plan:</h4>

                <table style={{ width: "100%", borderCollapse: "collapse", fontSize: "0.9rem" }}>
                  <thead>
                  <tr style={{ textAlign: "left" }}>
                    <th style={{ padding: "8px", borderBottom: "2px solid #ddd" }}>Office</th>
                    <th style={{ padding: "8px", borderBottom: "2px solid #ddd" }}>From</th>
                    <th style={{ padding: "8px", borderBottom: "2px solid #ddd" }}>To</th>
                    <th style={{ padding: "8px", borderBottom: "2px solid #ddd" }}>Action</th>
                  </tr>
                  </thead>

                  <tbody>
                  {doctor.shifts && doctor.shifts.length > 0 ? (
                      doctor.shifts.map((shift) => (
                          <tr key={shift.id} style={{ borderBottom: "1px solid #eee" }}>
                            <td style={{ padding: "8px" }}>
                              <strong>{shift.officeName}</strong>
                            </td>
                            <td style={{ padding: "8px" }}>{formatDate(shift.start)}</td>
                            <td style={{ padding: "8px" }}>{formatDate(shift.end)}</td>
                            <td style={{ padding: "8px" }}>
                              <button
                                  onClick={() => void handleDeleteShift(shift.id)}
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
                            style={{ padding: "12px", textAlign: "center", color: "#888" }}
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
