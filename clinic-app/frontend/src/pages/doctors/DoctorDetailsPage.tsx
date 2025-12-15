import { useState } from "react";
import { getDoctor } from "../../api/doctors";
import type { DoctorDetailsDTO } from "../../types/doctor.ts";

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

    return (
        <div>
            <h2>Doctor details</h2>

            <div style={{ display: "flex", gap: 10, alignItems: "center", flexWrap: "wrap" }}>
                <input
                    value={id}
                    onChange={(e) => setId(e.target.value)}
                    style={{ width: 160 }}
                />
                <button onClick={() => void load()}>Find</button>
            </div>

            {msg && <p style={{ marginTop: 12, color: "crimson" }}>{msg}</p>}

            {doctor && (
                <div style={{ marginTop: 12, padding: 12, border: "1px solid #ddd", borderRadius: 12 }}>
                    <h3 style={{ marginTop: 0 }}>
                        {doctor.firstName} {doctor.lastName}
                    </h3>
                    <p><strong>specialization:</strong> {doctor.specialization}</p>
                    <p><strong>addres:</strong> {doctor.address}</p>
                </div>
            )}
        </div>
    );
}
