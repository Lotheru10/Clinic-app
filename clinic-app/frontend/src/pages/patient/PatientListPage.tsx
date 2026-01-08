import {useEffect, useState} from "react";
import type {PatientDTO} from "../../types/patient.ts";
import {getPatients} from "../../api/patients.ts";


export default function PatientsListPage(){
    const [patients, setPatients] = useState<PatientDTO[]>([]);
    const [error, setError] = useState<string>("");
    const [loading, setLoading]  = useState<boolean>(true);

    const load = async () => {
        try {
            setError("");
            setLoading(true);
            const data = await getPatients();
            setPatients(data);
        } catch (e){
            setError(e instanceof Error ? e.message : "Patients loading problem")
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
            <h2>Patients List</h2>
            <button onClick={() => void load()}>Refresh</button>

            <ul>
                {patients.map((d, i) => (
                    <li key={i}>
                        {d.id} {d.firstName} {d.lastName}
                    </li>
                ))}
            </ul>
        </div>
    )
}