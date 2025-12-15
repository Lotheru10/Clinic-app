import { NavLink, Routes, Route, Navigate } from "react-router-dom";
import DoctorsListPage from "./pages/doctors/DoctorListPage.tsx";
import LoadDoctorsPage from "./pages/doctors/LoadDoctorsPage.tsx";
import DoctorAddPage from "./pages/doctors/DoctorAddPage.tsx";
import DoctorDeletePage from "./pages/doctors/DoctorDeletePage.tsx";
import DoctorDetailsPage from "./pages/doctors/DoctorDetailsPage.tsx";
function App() {

  return (
      <div>
          <header>
            <h1>M1 - Doctors</h1>
          </header>
          <nav style={{ display: "flex", gap: 20, flexWrap: "wrap", marginBottom: 16 }}>
              <NavLink to="/doctors">Doctors list</NavLink>
              <NavLink to="/doctors/details">Doctor details</NavLink>
              <NavLink to="/doctors/add">Add doctor</NavLink>
              <NavLink to="/doctors/delete">Delete doctor</NavLink>
              <NavLink to="/doctors/load">Load doctors</NavLink>
          </nav>
          <div>
              <Routes>
                  <Route path="/" element={<Navigate to="/doctors"/>}></Route>
                  <Route path="/doctors" element={<DoctorsListPage />} />
                  <Route path="/doctors/details" element={<DoctorDetailsPage />} />
                  <Route path="/doctors/add" element={<DoctorAddPage />} />
                  <Route path="/doctors/delete" element={<DoctorDeletePage />} />
                  <Route path="/doctors/load" element={<LoadDoctorsPage/>} />
              </Routes>
          </div>
      </div>
  )
}

export default App
