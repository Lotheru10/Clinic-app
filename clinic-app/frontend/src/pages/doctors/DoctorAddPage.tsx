import { useState } from "react";
import { addDoctor } from "../../api/doctors";
import {type DoctorDTO, type DoctorCreateRequest, DoctorSpecialization} from "../../types/doctor.ts"

const initial: DoctorCreateRequest = {
    firstName: "",
    lastName: "",
    peselNumber: "",
    specialization: "",
    address: "",
};

export default function DoctorAddPage() {
    const [form, setForm] = useState<DoctorCreateRequest>(initial);
    const [msg, setMsg] = useState<string>("");
    const [created, setCreated] = useState<DoctorDTO | null>(null);

    const onChange =
        (k: keyof DoctorCreateRequest) => (e: React.ChangeEvent<HTMLInputElement>) => {
            setForm((p) => ({ ...p, [k]: e.target.value }));
        };


    const submit = async (e: React.FormEvent) => {
        e.preventDefault();
        setMsg("");
        setCreated(null);

        try {
            const res = await addDoctor(form);
            setCreated(res);
            setMsg("Doctor added");
            setForm(initial);
        } catch (e) {
            setMsg(`Error: ${e instanceof Error ? e.message : "unknown"}`);
        }
    };

    return (
        <div>
            <h2>Add doctor</h2>

            <form onSubmit={submit} style={{ display: "grid", gap: 10, maxWidth: 420 }}>
                <label>First name:</label>
                <input value={form.firstName} onChange={onChange("firstName")} />
                <label>Last name:</label>
                <input value={form.lastName} onChange={onChange("lastName")} />
                <label>Pesel:</label>
                <input value={form.peselNumber} onChange={onChange("peselNumber")} />
                <label>Specialization:</label>
                <select
                    value={form.specialization}
                    onChange={(e) =>
                        setForm((p) => ({
                            ...p,
                            specialization: e.target.value as DoctorSpecialization,
                        }))
                    }
                    required
                >
                    <option value=""></option>

                    {Object.values(DoctorSpecialization).map((s) => (
                        <option key={s} value={s}>
                            {s}
                        </option>
                    ))}
                </select>                <label>Address:</label>
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
                        {created.firstName} {created.lastName} — {created.specialization}
                    </div>
                </div>
            )}
        </div>
    );
}
