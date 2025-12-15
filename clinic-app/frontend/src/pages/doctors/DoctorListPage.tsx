import {useEffect, useState} from "react";
import type {DoctorDTO} from "../../types/doctor.ts";
import {getDoctors} from "../../api/doctors.ts";


export default function DoctorsListPage(){
    const [doctors, setDoctors] = useState<DoctorDTO[]>([]);
    const [error, setError] = useState<string>("");
    const [loading, setLoading]  = useState<boolean>(true);

    const load = async () => {
        try {
            setError("");
            setLoading(true);
            const data = await getDoctors();
            setDoctors(data);
        } catch (e){
            setError(e instanceof Error ? e.message : "Doctors loading problem")
        } finally {
            setLoading(false);
        }
    };
    useEffect(()=> {
        void load();
    }, []);
    if (loading) return <p>Loading</p>
    if (error) return <p>{error}</p>

    return (
        <div>
            <h2>Doctors List</h2>
            <button onClick={() => void load()}>Refresh</button>

            <ul>
                {doctors.map((d, i) => (
                    <li key={i}>
                        {d.id} {d.firstName} {d.lastName} {d.specialization}
                    </li>
                ))}
            </ul>
        </div>
    )
}