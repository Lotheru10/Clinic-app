import { useState } from "react";
import { deleteDoctor } from "../../api/doctors";

export default function DoctorDeletePage() {
    const [id, setId] = useState<string>("");
    const [msg, setMsg] = useState<string>("");

    const runDelete = async () => {
        setMsg("");

        const parsed = Number(id);

        try {
            await deleteDoctor(parsed);
            setMsg("Deleted");
            setId("");
        } catch (e) {
            setMsg(`Error: ${e instanceof Error ? e.message : "unknown"}`);
        }
    };

    return (
        <div>
            <h2>Delete doctor</h2>

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
