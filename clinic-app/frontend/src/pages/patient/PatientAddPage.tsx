import { useState } from "react";
import { addPatient } from "../../api/patients";
import {type PatientDTO, type PatientCreateRequest} from "../../types/patient.ts"

const initial: PatientCreateRequest = {
    firstName: "",
    lastName: "",
    peselNumber: "",
    address: "",
};

export default function PatientAddPage() {
    const [form, setForm] = useState<PatientCreateRequest>(initial);
    const [msg, setMsg] = useState<string>("");
    const [created, setCreated] = useState<PatientDTO | null>(null);

    const onChange =
        (k: keyof PatientCreateRequest) => (e: React.ChangeEvent<HTMLInputElement>) => {
            setForm((p) => ({ ...p, [k]: e.target.value }));
        };


    const submit = async (e: React.FormEvent) => {
        e.preventDefault();
        setMsg("");
        setCreated(null);

        try {
            const res = await addPatient(form);
            setCreated(res);
            setMsg("Patient added");
            setForm(initial);
        } catch (e) {
            setMsg(`Error: ${e instanceof Error ? e.message : "unknown"}`);
        }
    };

    return (
        <div>
            <h2>Add patient</h2>

            <form onSubmit={submit} style={{ display: "grid", gap: 10, maxWidth: 420 }}>
                <label>First name:</label>
                <input value={form.firstName} onChange={onChange("firstName")} />
                <label>Last name:</label>
                <input value={form.lastName} onChange={onChange("lastName")} />
                <label>Pesel:</label>
                <input value={form.peselNumber} onChange={onChange("peselNumber")} />
                <label>Address:</label>
                <input value={form.address} onChange={onChange("address")} />
                <button type="submit">SUBMIT</button>
            </form>

            {msg && <p style={{ marginTop: 12 }}>{msg}</p>}

            {created && (
                <div style={{ marginTop: 12, padding: 12, border: "1px solid #ddd", borderRadius: 12 }}>
                    <strong>CREATED</strong>
                    <div>
                        Id: {created.id}
                        <br/>
                        {created.firstName} {created.lastName}
                    </div>
                </div>
            )}
        </div>
    );
}
