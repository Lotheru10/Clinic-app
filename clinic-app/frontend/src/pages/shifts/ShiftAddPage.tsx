import { useState } from "react";
import { checkAvailability, createShift } from "../../api/shifts.ts";
import type {AvailabilityDTO, ShiftDTO} from "../../types/shift.ts";

export default function ShiftAddPage() {

    const [start, setStart] = useState("");
    const [end, setEnd] = useState("");

    const [availability, setAvailability] = useState<AvailabilityDTO | null>(null);
    const [isSearched, setIsSearched] = useState(false);
    const [isLoading, setIsLoading] = useState(false);

    const [selectedDocId, setSelectedDocId] = useState<number | "">("");
    const [selectedOfficeId, setSelectedOfficeId] = useState<number | "">("");

    const [msg, setMsg] = useState("");
    const [createdShift, setCreatedShift] = useState<ShiftDTO | null>(null);


    const handleCheck = async () => {
        if (!start || !end) {
            setMsg("Enter start and end date");
            return;
        }

        setIsLoading(true);
        setMsg("");
        setAvailability(null);

        try {
            const data = await checkAvailability(start, end);
            setAvailability(data);
            setIsSearched(true);
        } catch (e) {
            setMsg("Error");
            console.error(e);
        } finally {
            setIsLoading(false);
        }
    };

    const handleSave = async () => {
        if (!selectedDocId || !selectedOfficeId) return;

        try {
            const shift = await createShift({
                doctorId: Number(selectedDocId),
                officeId: Number(selectedOfficeId),
                start,
                end
            });

            setCreatedShift(shift);
        } catch (e) {
            setMsg("Could not add shift.");
            console.error(e);
        }
    };

    return (
        <div style={{ maxWidth: 600, margin: "0 auto", padding: 20 }}>

            <h2>Plan a shift</h2>

            <div style={{padding: 20, borderRadius: 8, marginBottom: 20 }}>
                <div style={{ marginBottom: 15 }}>
                    <label style={{display: "block", marginBottom: 5}}>Start:</label>
                    <input
                        type="datetime-local"
                        value={start}
                        onChange={e => { setStart(e.target.value); setIsSearched(false); }}
                        style={{ width: "100%", padding: 8, boxSizing: "border-box" }}
                    />
                </div>
                <div style={{ marginBottom: 15 }}>
                    <label style={{display: "block", marginBottom: 5}}>End:</label>
                    <input
                        type="datetime-local"
                        value={end}
                        onChange={e => { setEnd(e.target.value); setIsSearched(false); }}
                        style={{ width: "100%", padding: 8, boxSizing: "border-box" }}
                    />
                </div>
                <button
                    onClick={handleCheck}
                    disabled={isLoading}
                    style={{ cursor: "pointer", padding: "10px 20px", background: "#007bff", color: "white", border: "none", borderRadius: 4 }}
                >
                    {isLoading ? "Searching..." : "Check availability"}
                </button>
            </div>

            {msg && <p style={{ color: "crimson", fontWeight: "bold" }}>{msg}</p>}


            {isSearched && availability && (
                <div style={{ border: "1px solid #ddd", padding: 20, borderRadius: 8}}>
                    <h3>Avaiable:</h3>

                    <div style={{ marginBottom: 15 }}>
                        <label style={{display: "block", marginBottom: 5}}>Choose a doctor:</label>
                        <select
                            value={selectedDocId}
                            onChange={e => setSelectedDocId(Number(e.target.value))}
                            style={{ display: "block", width: "100%", padding: 8 }}
                        >
                            <option value="">-- Choose --</option>
                            {availability.freeDoctors.map(doc => (
                                <option key={doc.id} value={doc.id}>
                                    {doc.firstName} {doc.lastName}
                                </option>
                            ))}
                        </select>
                        {availability.freeDoctors.length === 0 && <small style={{color:'red'}}>No free doctors at this time!</small>}
                    </div>


                    <div style={{ marginBottom: 20 }}>
                        <label style={{display: "block", marginBottom: 5}}>Choose an office:</label>
                        <select
                            value={selectedOfficeId}
                            onChange={e => setSelectedOfficeId(Number(e.target.value))}
                            style={{ display: "block", width: "100%", padding: 8 }}
                        >
                            <option value="">-- Choose --</option>
                            {availability.freeDoctorsOffices.map(office => (
                                <option key={office.id} value={office.id}>
                                    Office {office.roomNumber}
                                </option>
                            ))}
                        </select>
                        {availability.freeDoctorsOffices.length === 0 && <small style={{color:'red'}}>No free office at this time!</small>}
                    </div>

                    <button
                        onClick={handleSave}
                        disabled={!selectedDocId || !selectedOfficeId}
                        style={{
                            background: (!selectedDocId || !selectedOfficeId) ? "#ccc" : "#28a745",
                            color: "white", padding: "12px 20px", border: "none", borderRadius: 4, cursor: "pointer", width: "100%", fontSize: "1rem"
                        }}
                    >
                        Confirm
                    </button>
                </div>
            )}
            {createdShift && (
                <div
                    style={{
                        marginBottom: 20,
                        padding: 16,
                        background: "#e6fffa",
                        borderRadius: 8,
                        border: "1px solid #38b2ac",
                        color: "#065f46"
                    }}
                >
                    <h3 style={{ marginTop: 0 }}>Shift created successfully </h3>
                    <p><strong>ID:</strong> {createdShift.id}</p>
                    <p><strong>Doctor:</strong> {createdShift.doctorName}</p>
                    <p><strong>Office:</strong> {createdShift.officeName}</p>
                    <p><strong>Time:</strong> {createdShift.start} - {createdShift.end}</p>
                </div>
            )}
        </div>
    );
}