import { useMemo, useState } from "react";
import { getPatient } from "../../api/patients";
import { deleteAppointment } from "../../api/appointment";
import { addOpinion } from "../../api/opinion";
import type { PatientDetailsDTO } from "../../types/patient.ts";
import type { CreateOpinionDTO, OpinionDTO } from "../../types/opinion.ts";

function StarRating({
                      value,
                      onChange,
                      disabled,
                    }: {
  value: number;
  onChange: (v: number) => void;
  disabled?: boolean;
}) {
  return (
      <div style={{ display: "inline-flex", gap: 4, userSelect: "none" }}>
        {[1, 2, 3, 4, 5].map((star) => (
            <button
                key={star}
                type="button"
                disabled={disabled}
                onClick={() => onChange(star)}
                aria-label={`${star} star`}
                style={{
                  border: "none",
                  background: "transparent",
                  cursor: disabled ? "default" : "pointer",
                  fontSize: 20,
                  lineHeight: 1,
                  padding: 0,
                  opacity: disabled ? 0.6 : 1,
                }}
            >
              {star <= value ? "★" : "☆"}
            </button>
        ))}
      </div>
  );
}

export default function PatientDetailsPage() {
  const [id, setId] = useState<string>("");
  const [patient, setPatient] = useState<PatientDetailsDTO | null>(null);
  const [msg, setMsg] = useState<string>("");

  // --- Opinion form state
  const [opinionForAppointmentId, setOpinionForAppointmentId] = useState<number | null>(null);
  const [rate, setRate] = useState<number>(0);
  const [message, setMessage] = useState<string>("");
  const [savingOpinion, setSavingOpinion] = useState<boolean>(false);

  const load = async () => {
    setMsg("");
    setPatient(null);
    setOpinionForAppointmentId(null);

    const parsed = Number(id);
    try {
      const data = await getPatient(parsed);
      setPatient(data);
    } catch (e) {
      setMsg(`Error: ${e instanceof Error ? e.message : "unknown"}`);
    }
  };

  const handleCancelAppointment = async (appointmentId: number) => {
    if (!confirm("Are you sure you want to cancel this appointment?")) return;

    try {
      await deleteAppointment(appointmentId);
      await load();
    } catch (e) {
      setMsg(
          `Error cancelling appointment: ${e instanceof Error ? e.message : "unknown"}`,
      );
    }
  };

  const handleAddingOpinion = (appointmentId: number) => {
    setMsg("");
    setOpinionForAppointmentId(appointmentId);
    setRate(0);
    setMessage("");
  };

  const submitOpinion = async () => {
    if (!opinionForAppointmentId) return;

    if (rate < 1 || rate > 5) {
      setMsg("Please select a rating (1–5).");
      return;
    }

    const payload: CreateOpinionDTO = {
      rate,
      message,
      appointmentId: opinionForAppointmentId,
    };

    try {
      setSavingOpinion(true);
      console.log(payload);
      await addOpinion(payload);
      setOpinionForAppointmentId(null);
      await load();
    } catch (e) {
      setMsg(`Error adding opinion: ${e instanceof Error ? e.message : "unknown"}`);
    } finally {
      setSavingOpinion(false);
    }
  };

  const formatDate = (dateString: string) => {
    if (!dateString) return "-";
    return new Date(dateString).toLocaleString("en-GB", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const isPast = (start: string) => new Date(start).getTime() < Date.now();

  const selectedAppointment = useMemo(() => {
    if (!patient || !opinionForAppointmentId) return null;
    return patient.appointments?.find((a) => a.id === opinionForAppointmentId) ?? null;
  }, [patient, opinionForAppointmentId]);

  const renderOpinionPreview = (opinion: OpinionDTO) => {
    const stars = "★".repeat(opinion.rate) + "☆".repeat(5 - opinion.rate);
    return (
        <span title={opinion.message || ""} style={{ whiteSpace: "nowrap" }}>
        {stars}
      </span>
    );
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
                  <tr style={{textAlign: "left" }}>
                    <th style={{ padding: "8px", borderBottom: "2px solid #ddd" }}>
                      Date
                    </th>
                    <th style={{ padding: "8px", borderBottom: "2px solid #ddd" }}>
                      Doctor
                    </th>
                    <th style={{ padding: "8px", borderBottom: "2px solid #ddd" }}>
                      Office
                    </th>
                    <th style={{ padding: "8px", borderBottom: "2px solid #ddd" }}>
                      Action
                    </th>
                  </tr>
                  </thead>

                  <tbody>
                  {patient.appointments && patient.appointments.length > 0 ? (
                      patient.appointments.map((appt) => {
                        const past = isPast(appt.start);
                        const alreadyRated = !!appt.opinion; // <- tu zakładam, że backend zwraca opinion

                        return (
                            <tr key={appt.id} style={{ borderBottom: "1px solid #eee" }}>
                              <td style={{ padding: "8px" }}>{formatDate(appt.start)}</td>
                              <td style={{ padding: "8px" }}>{appt.doctorName}</td>
                              <td style={{ padding: "8px" }}>{appt.officeName}</td>

                              <td style={{ padding: "8px" }}>
                                {/* Past appointment -> opinion */}
                                {past ? (
                                    alreadyRated ? (
                                        <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
                                          <span style={{ color: "#666" }}>Rated:</span>
                                          {renderOpinionPreview(appt.opinion!)}
                                        </div>
                                    ) : (
                                        <button
                                            onClick={() => handleAddingOpinion(appt.id)}
                                            style={{
                                              backgroundColor: "#4f46e5",
                                              color: "white",
                                              border: "none",
                                              padding: "5px 10px",
                                              cursor: "pointer",
                                              borderRadius: "4px",
                                            }}
                                        >
                                          Add opinion
                                        </button>
                                    )
                                ) : (
                                    /* Future appointment -> cancel */
                                    <button
                                        onClick={() => handleCancelAppointment(appt.id)}
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
                                )}
                              </td>
                            </tr>
                        );
                      })
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

                {/* FORMULARZ OPINII POD TABELĄ */}
                {opinionForAppointmentId && (
                    <div
                        style={{
                          marginTop: 14,
                          padding: 12,
                          border: "1px solid #e5e7eb",
                          borderRadius: 12,
                        }}
                    >
                      <div style={{ display: "flex", justifyContent: "space-between", gap: 10 }}>
                        <h4 style={{ margin: 0 }}>
                          Add opinion for appointment #{opinionForAppointmentId}
                        </h4>
                        <button
                            type="button"
                            onClick={() => setOpinionForAppointmentId(null)}
                            style={{
                              border: "none",
                              background: "transparent",
                              cursor: "pointer",
                              fontSize: 16,
                            }}
                            aria-label="Close"
                            title="Close"
                        >
                          ✕
                        </button>
                      </div>

                      {selectedAppointment && (
                          <p style={{ marginTop: 8, marginBottom: 10, color: "#EEE" }}>
                            <strong>{formatDate(selectedAppointment.start)}</strong> •{" "}
                            {selectedAppointment.doctorName} • {selectedAppointment.officeName}
                          </p>
                      )}

                      <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
                        <span style={{ minWidth: 60 }}><strong>Rating:</strong></span>
                        <StarRating value={rate} onChange={setRate} disabled={savingOpinion} />
                      </div>

                      <div style={{ marginTop: 10 }}>
                        <div style={{ marginBottom: 6 }}><strong>Message:</strong></div>
                        <textarea
                            value={message}
                            onChange={(e) => setMessage(e.target.value)}
                            rows={3}
                            placeholder="Optional comment…"
                            disabled={savingOpinion}
                            style={{
                              width: "100%",
                              resize: "vertical",
                              padding: 10,
                              borderRadius: 8,
                              border: "1px solid #ddd",
                            }}
                        />
                      </div>

                      <div style={{ marginTop: 10, display: "flex", gap: 10 }}>
                        <button
                            type="button"
                            onClick={() => void submitOpinion()}
                            disabled={savingOpinion}
                            style={{
                              backgroundColor: savingOpinion ? "#9ca3af" : "#16a34a",
                              color: "white",
                              border: "none",
                              padding: "7px 12px",
                              cursor: savingOpinion ? "default" : "pointer",
                              borderRadius: "6px",
                            }}
                        >
                          {savingOpinion ? "Saving..." : "Submit opinion"}
                        </button>

                        <button
                            type="button"
                            onClick={() => setOpinionForAppointmentId(null)}
                            disabled={savingOpinion}
                            style={{
                              backgroundColor: "white",
                              color: "#111",
                              border: "1px solid #ddd",
                              padding: "7px 12px",
                              cursor: savingOpinion ? "default" : "pointer",
                              borderRadius: "6px",
                            }}
                        >
                          Cancel
                        </button>
                      </div>
                    </div>
                )}
              </div>
            </div>
        )}
      </div>
  );
}
