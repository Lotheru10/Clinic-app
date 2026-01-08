import {useEffect, useState} from "react";
import type {DoctorsOfficeDTO} from "../../types/doctorsoffice.ts";
import {getDoctorsOffices} from "../../api/doctorsoffices.ts";


export default function
    DoctorsOfficeListPage(){
    const [doctors, setDoctors] = useState<DoctorsOfficeDTO[]>([]);
    const [error, setError] = useState<string>("");
    const [loading, setLoading]  = useState<boolean>(true);

    const load = async () => {
        try {
            setError("");
            setLoading(true);
            const data = await getDoctorsOffices();
            setDoctors(data);
        } catch (e){
            setError(e instanceof Error ? e.message : "Doctor's office loading problem")
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
            <h2>Doctors' offices List</h2>
            <button onClick={() => void load()}>Refresh</button>

            <ul>
                {doctors.map((d, i) => (
                    <li key={i}>
                        {d.id} {d.roomNumber}
                    </li>
                ))}
            </ul>
        </div>
    )
}