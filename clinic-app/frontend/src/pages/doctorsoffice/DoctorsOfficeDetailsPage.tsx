import { useState } from "react";
import {getDoctorsOffice} from "../../api/doctorsoffices.ts";
import type {DoctorsOfficeDetailsDTO} from "../../types/doctorsoffice.ts";

export default function DoctorsOfficeDetailsPage() {
    const [id, setId] = useState<string>("");
    const [doctorsOffice, setDoctorsOffice] = useState<DoctorsOfficeDetailsDTO | null>(null);
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

    const formatDate = (dateString: string) => {
        if (!dateString) return "";
        return new Date(dateString).toLocaleString("pl-PL", {
            day: '2-digit', month: '2-digit', year: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
    };

    return (
        <div>
            <h2>Doctor's office details</h2>

            <div style={{ display: "flex", gap: 10, alignItems: "center", flexWrap: "wrap" }}>
                <input
                    value={id}
                    onChange={(e) => setId(e.target.value)}
                    style={{ width: 160 }}
                />
                <button onClick={() => void load()}>Find</button>
            </div>

            {msg && <p style={{ marginTop: 12, color: "crimson" }}>{msg}</p>}

            {doctorsOffice && (
                <div style={{ marginTop: 12, padding: 12, border: "1px solid #ddd", borderRadius: 12 }}>
                    <h3 style={{ marginTop: 0 }}>
                        {doctorsOffice.roomNumber}
                    </h3>
                    <p><strong>description:</strong> {doctorsOffice.roomDescription}</p>

                    <div>
                        <strong>Harmonogram dyżurów:</strong>
                        {doctorsOffice.shifts && doctorsOffice.shifts.length > 0 ? (
                            <ul style={{ marginTop: 5 }}>
                                {doctorsOffice.shifts.map((shift, index) => (
                                    <li key={index} style={{ marginBottom: 5 }}>
                                        <b>{shift.doctorName}</b> <br/>
                                        <span style={{ fontSize: "0.9em", color: "#555" }}>
                                            {formatDate(shift.start)} - {formatDate(shift.end)}
                                        </span>
                                    </li>
                                ))}
                            </ul>
                        ) : (
                            <p>Brak dyżurów.</p>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}
