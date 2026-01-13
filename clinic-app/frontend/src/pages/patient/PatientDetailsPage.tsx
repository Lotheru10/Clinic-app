import { useState } from "react";
import { getPatient } from "../../api/patients";
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

    return (
        <div>
            <h2>Patient details</h2>

            <div style={{ display: "flex", gap: 10, alignItems: "center", flexWrap: "wrap" }}>
                <input
                    value={id}
                    onChange={(e) => setId(e.target.value)}
                    style={{ width: 160 }}
                />
                <button onClick={() => void load()}>Find</button>
            </div>

            {msg && <p style={{ marginTop: 12, color: "crimson" }}>{msg}</p>}

            {patient && (
                <div style={{ marginTop: 12, padding: 12, border: "1px solid #ddd", borderRadius: 12 }}>
                    <h3 style={{ marginTop: 0 }}>
                        {patient.firstName} {patient.lastName}
                    </h3>
                    <p><strong>addres:</strong> {patient.address}</p>
                </div>
            )}
        </div>
    );
}
