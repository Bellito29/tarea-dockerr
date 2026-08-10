import { createBrowserRouter, Navigate, RouterProvider } from "react-router";
import Login from "../pages/login/Login";
import ProtectedRoute from "../components/common/ProtectedRoute";
import TrainerLayout from "../components/layout/TrainerLayout";
import Routines from "../pages/trainer/routines/Routines";
import Exercises from "../pages/trainer/exercises/Exercises";
import Sesions from "../pages/trainer/sesions/Sesions";
import Students from "../pages/trainer/students/Students";
import Recomendations from "../pages/trainer/recomendations/Recomendations";
import Messages from "../pages/trainer/messages/Messages";
import Stats from "../pages/trainer/stats/Stats";
import AdminDashboard from "../pages/admin/AdminDashboard";
import Users from "../pages/admin/AdminUsers";
import StudentDashboard from '../pages/student/StudentDashboard';
import Roles from "../pages/admin/AdminRoles";
import Permissions from "../pages/admin/AdminPermissions";

const router = createBrowserRouter([
    {
        path: '/',
        element: <Login />,
    },
    {
        path: "/admin",
        element: (
            <ProtectedRoute>
                <AdminDashboard />
            </ProtectedRoute>
        ),

        children: [
            { path: "users", element: <Users /> },
            { path: "roles", element: <Roles /> },
            { path: "permissions", element: <Permissions /> },
        ],
      },
    {
        path: '/studentDashboard',
        element: (
            <ProtectedRoute>
                <StudentDashboard />
            </ProtectedRoute>
        ),
    },
    {
        path: '/trainer',
        element: (
            <ProtectedRoute>
                <TrainerLayout />
            </ProtectedRoute>
        ),
        children: [
            { index: true, element: <Navigate to="routines" replace /> },
            { path: 'routines', element: <Routines /> },
            { path: 'exercises', element: <Exercises /> },
            { path: 'sesions', element: <Sesions /> },
            { path: 'students', element: <Students /> },
            { path: 'recomendations', element: <Recomendations /> },
            { path: 'messages', element: <Messages /> },
            { path: 'stats', element: <Stats /> },
        ],
    },
], { basename: '/tallerfrontLJJJ' });

function AppRouter() {
    return <RouterProvider router={router} />;
}

export default AppRouter;