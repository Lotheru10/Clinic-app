import { NavLink, Routes, Route, Navigate } from "react-router-dom";
import DoctorsListPage from "./pages/doctors/DoctorListPage.tsx";
import LoadDoctorsPage from "./pages/doctors/LoadDoctorsPage.tsx";
import DoctorAddPage from "./pages/doctors/DoctorAddPage.tsx";
import DoctorDeletePage from "./pages/doctors/DoctorDeletePage.tsx";
import DoctorDetailsPage from "./pages/doctors/DoctorDetailsPage.tsx";
import PatientsListPage from "./pages/patient/PatientListPage.tsx";
import PatientDetailsPage from "./pages/patient/PatientDetailsPage.tsx";
import PatientAddPage from "./pages/patient/PatientAddPage.tsx";
import PatientDeletePage from "./pages/patient/PatientDeletePage.tsx";
import DoctorsOfficeListPage from "./pages/doctorsoffice/DoctorsOfficeListPage.tsx";
import DoctorsOfficeDetailsPage from "./pages/doctorsoffice/DoctorsOfficeDetailsPage.tsx";
import DoctorsOfficeAddPage from "./pages/doctorsoffice/DoctorsOfficeAddPage.tsx";
import DoctorsOfficeDeletePage from "./pages/doctorsoffice/DoctorsOfficeDeletePage.tsx";
import ShiftAddPage from "./pages/shifts/ShiftAddPage.tsx";

function App() {

  return (
      <div>
          <header>
            <h1>Clinic App</h1>
          </header>

          <nav>

              <div>
                  <h3 >Doctors</h3>
                  <div style={{ display: "flex", gap: "15px", flexWrap: "wrap" }}>
                      <NavLink to="/doctors">Doctors list</NavLink>
                      <NavLink to="/doctors/details">Doctor details</NavLink>
                      <NavLink to="/doctors/add">Add doctor</NavLink>
                      <NavLink to="/doctors/delete">Delete doctor</NavLink>
                      <NavLink to="/doctors/load">Load sample doctors</NavLink>
                  </div>
              </div>

              <div>
                  <h3>Patients</h3>
                  <div style={{ display: "flex", gap: "15px", flexWrap: "wrap" }}>
                      <NavLink to="/patients">Patients list</NavLink>
                      <NavLink to="/patients/details">Patients details</NavLink>
                      <NavLink to="/patients/add">Add patient</NavLink>
                      <NavLink to="/patients/delete">Delete patient</NavLink>
                  </div>
              </div>

              <div>
                  <h3>Doctor's office</h3>
                  <div style={{ display: "flex", gap: "15px", flexWrap: "wrap" }}>
                      <NavLink to="/doctorsOffices">Doctor's offices list</NavLink>
                      <NavLink to="/doctorsOffices/details">Doctor's offices details</NavLink>
                      <NavLink to="/doctorsOffices/add">Add doctor's office</NavLink>
                      <NavLink to="/doctorsOffices/delete">Delete doctor's office</NavLink>
                  </div>
              </div>

              <div>
                  <h3>Shift</h3>
                  <div style={{ display: "flex", gap: "15px", flexWrap: "wrap" }}>
                      <NavLink to="/shifts/create">Add shift</NavLink>
                  </div>
              </div>

          </nav>


          <div>
              <Routes>
                  <Route path="/" element={<Navigate to="/doctors"/>}></Route>
                  <Route path="/doctors" element={<DoctorsListPage />} />
                  <Route path="/doctors/details" element={<DoctorDetailsPage />} />
                  <Route path="/doctors/add" element={<DoctorAddPage />} />
                  <Route path="/doctors/delete" element={<DoctorDeletePage />} />
                  <Route path="/doctors/load" element={<LoadDoctorsPage/>} />

                  <Route path="/patients" element={<PatientsListPage />} />
                  <Route path="/patients/details" element={<PatientDetailsPage />} />
                  <Route path="/patients/add" element={<PatientAddPage />} />
                  <Route path="/patients/delete" element={<PatientDeletePage />} />

                  <Route path="/doctorsOffices" element={<DoctorsOfficeListPage />} />
                  <Route path="/doctorsOffices/details" element={<DoctorsOfficeDetailsPage />} />
                  <Route path="/doctorsOffices/add" element={<DoctorsOfficeAddPage />} />
                  <Route path="/doctorsOffices/delete" element={<DoctorsOfficeDeletePage />} />

                  <Route path="/shifts/create" element={<ShiftAddPage />} />
              </Routes>
          </div>
      </div>
  )
}

export default App
