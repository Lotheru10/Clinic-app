import { useState } from "react";
import { deletePatient } from "../../api/patients";

export default function PatientDeletePage() {
    const [id, setId] = useState<string>("");
    const [msg, setMsg] = useState<string>("");

    const runDelete = async () => {
        setMsg("");

        const parsed = Number(id);

        try {
            await deletePatient(parsed);
            setMsg("Deleted");
            setId("");
        } catch (e) {
            setMsg(`Error: ${e instanceof Error ? e.message : "unknown"}`);
        }
    };

    return (
        <div>
            <h2>Delete patient</h2>

            <div style={{ display: "flex", gap: 10, alignItems: "center", flexWrap: "wrap" }}>
                <input
                    value={id}
                    onChange={(e) => setId(e.target.value)}
                    style={{ width: 160 }}
                />
                <button onClick={() => void runDelete()}>Delete</button>
            </div>

            {msg && <p style={{ marginTop: 12 }}>{msg}</p>}
        </div>
    );
}
