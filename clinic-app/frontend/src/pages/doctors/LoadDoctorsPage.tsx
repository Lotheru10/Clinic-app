import { useState } from 'react';
import {addExampleDoctors} from "../../api/doctors.ts";

export default function LoadDoctorsPage(){
    const [msg, setMsg] = useState<string>("");

    const seed = async () => {
        try {
            setMsg("Adding");
            await addExampleDoctors();
            setMsg("Added doctors");
        }
        catch (e){
            setMsg(`Error: ${e instanceof Error ? e.message : "unknown"}`);
        }
    };

    return (
        <div>
            <h2>Load doctors</h2>
            <button onClick={() => void seed()}> Load doctors </button>
            {msg && <p>{msg}</p>}
        </div>
    )
}