import { useState } from "react";
import {addDoctorsOffice} from "../../api/doctorsoffices.ts";
import type {DoctorsOfficeCreateRequest, DoctorsOfficeDTO} from "../../types/doctorsoffice.ts";

const initial: DoctorsOfficeCreateRequest = {
    roomNumber: "",
    roomDescription: "",
};

export default function DoctorsOfficeAddPage() {
    const [form, setForm] = useState<DoctorsOfficeCreateRequest>(initial);
    const [msg, setMsg] = useState<string>("");
    const [created, setCreated] = useState<DoctorsOfficeDTO | null>(null);

    const onChange =
        (k: keyof DoctorsOfficeCreateRequest) => (e: React.ChangeEvent<HTMLInputElement>) => {
            setForm((p) => ({ ...p, [k]: e.target.value }));
        };


    const submit = async (e: React.FormEvent) => {
        e.preventDefault();
        setMsg("");
        setCreated(null);

        try {
            const res = await addDoctorsOffice(form);
            setCreated(res);
            setMsg("DoctorsOffice added");
            setForm(initial);
        } catch (e) {
            setMsg(`Error: ${e instanceof Error ? e.message : "unknown"}`);
        }
    };

    return (
        <div>
            <h2>Add doctor's office</h2>

            <form onSubmit={submit} style={{ display: "grid", gap: 10, maxWidth: 420 }}>
                <label>Room Number:</label>
                <input value={form.roomNumber} onChange={onChange("roomNumber")} />
                <label>Room Description:</label>
                <input value={form.roomDescription} onChange={onChange("roomDescription")} />
                <button type="submit">SUBMIT</button>
            </form>

            {msg && <p style={{ marginTop: 12 }}>{msg}</p>}

            {created && (
                <div style={{ marginTop: 12, padding: 12, border: "1px solid #ddd", borderRadius: 12 }}>
                    <strong>CREATED</strong>
                    <div>
                        Id: {created.id}
                        <br/>
                        {created.roomNumber}
                    </div>
                </div>
            )}
        </div>
    );
}
