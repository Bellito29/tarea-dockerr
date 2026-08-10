import { useState, useEffect } from 'react';
import { Box, Typography } from '@mui/material';
import PeopleIcon from '@mui/icons-material/People';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import SchoolIcon from '@mui/icons-material/School';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import Sidebar from '../../components/layout/sidebar';
import StatCard from '../../components/ui/StatCard';
import SessionItem from '../../components/ui/SesionItem';
import NotificationItem from '../../components/ui/NotificationItem';
import { getUsers, getTrainers, getStudents, getSesions, getMessages } from './services/adminService';
import { Outlet } from 'react-router';

interface SesionItem {
    id: number;
    sesionDate: string;
    duration?: number;
    userName?: string;
    routineName?: string;
}

interface MessageItem {
    id: number;
    content?: string;
    notificationType?: string;
}

function AdminDashboard() {
    const [metrics, setMetrics] = useState({ users: 0, trainers: 0, students: 0, sesions: 0 });
    const [notifications, setNotifications] = useState<MessageItem[]>([]);

    const [sesions, setSesions] = useState<SesionItem[]>([]);

    useEffect(() => {
        getUsers().then(res => setMetrics(prev => ({ ...prev, users: res.data.length })));
        getTrainers().then(res => setMetrics(prev => ({ ...prev, trainers: res.data.length })));
        getStudents().then(res => setMetrics(prev => ({ ...prev, students: res.data.length })));
        getSesions().then(res => {
            const sorted = (res.data as SesionItem[]).sort((a, b) => new Date(b.sesionDate).getTime() - new Date(a.sesionDate).getTime());
            setSesions(sorted.slice(0, 5));
        });
        getMessages().then(res => {
            setNotifications((res.data as MessageItem[]).slice(0, 5));
        });
    }, []);

    return (
        <Box sx={{ display: 'flex', height: '100vh', bgcolor: 'grey.100' }}>
            <Sidebar />
            <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
                <Box sx={{
                    bgcolor: 'background.paper', borderBottom: '1px solid', borderColor: 'divider',
                    px: 2.5, py: 1.5, display: 'flex', justifyContent: 'space-between', alignItems: 'center',
                }}>
                    <Typography sx={{ fontSize: 18, fontWeight: 500 }}>Inicio</Typography>
                </Box>

                <Box sx={{ flex: 1, display: 'flex', overflow: 'hidden' }}>
                    <Box sx={{ flex: 1, overflowY: 'auto', p: 2, display: 'flex', flexDirection: 'column', gap: 2 }}>

                        {/* Métricas — ahora con datos reales */}
                        <Box sx={{ display: 'flex', gap: 1.5 }}>
                            <StatCard label="Total usuarios"    value={metrics.users}    sub="+4 esta semana"           icon={<PeopleIcon fontSize="small" />}        iconColor="#4f46e5" />
                            <StatCard label="Trainers activos"  value={metrics.trainers} sub="3 nuevos este mes"        icon={<FitnessCenterIcon fontSize="small" />}  iconColor="#0f6e56" />
                            <StatCard label="Estudiantes"       value={metrics.students} sub="87 con rutina asignada"   icon={<SchoolIcon fontSize="small" />}         iconColor="#ba7517" />
                            <StatCard label="Sesiones este mes" value={metrics.sesions}  sub="↑ 12% vs mes anterior"   icon={<CalendarMonthIcon fontSize="small" />}  iconColor="#993556" />
                        </Box>

                        {/* Sesiones recientes — estático por ahora */}
                        <Box sx={{ bgcolor: 'background.paper', border: '1px solid', borderColor: 'divider', borderRadius: 2, overflow: 'hidden' }}>
                            <Box sx={{ px: 2.5, py: 1.5, borderBottom: '1px solid', borderColor: 'divider', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                <Typography sx={{ fontSize: 14, fontWeight: 500 }}>Últimas sesiones y rutinas</Typography>
                                <Box sx={{ fontSize: 10, px: 1, py: 0.25, borderRadius: 99, border: '1px solid', borderColor: 'divider', color: 'text.secondary' }}>
                                    Tiempo real
                                </Box>
                            </Box>
                            {sesions.map((s) => (
                                <SessionItem
                                    key={s.id}
                                    initials={s.userName ? s.userName.split(' ').map(n => n[0]).join('').slice(0, 2).toUpperCase() : '??'}
                                    name={s.userName ?? 'Sin nombre'}
                                    date={new Date(s.sesionDate).toLocaleString('es-CO', { dateStyle: 'short', timeStyle: 'short' })}
                                    routineName={s.routineName ? `Rutina: ${s.routineName}` : 'Sin rutina'}
                                    duration={s.duration ? `${s.duration} min` : '-'}
                                    volume={null}
                                    exercises={[]}
                                    extraCount={0}/>
                                ))}
                        </Box>
                    </Box>

                    {/* Panel derecho */}
                    <Box sx={{
                        width: 250, minWidth: 250, borderLeft: '1px solid', borderColor: 'divider',
                        bgcolor: 'background.paper', overflowY: 'auto', p: 2,
                        display: 'flex', flexDirection: 'column', gap: 2,
                    }}>
                        <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 1, pb: 2, borderBottom: '1px solid', borderColor: 'divider' }}>
                            <Box sx={{ width: 56, height: 56, borderRadius: '50%', background: '#1a1a2e', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 18, fontWeight: 500, color: '#fff' }}>
                                AD
                            </Box>
                            <Typography sx={{ fontSize: 14, fontWeight: 500 }}>Administrador</Typography>
                            <Box sx={{ display: 'flex', gap: 2.5 }}>
                                {[
                                    [metrics.users,    'Usuarios'],
                                    [metrics.trainers, 'Trainers'],
                                    [metrics.students, 'Alumnos'],
                                ].map(([val, lbl]) => (
                                    <Box key={lbl} sx={{ textAlign: 'center' }}>
                                        <Typography sx={{ fontSize: 15, fontWeight: 500 }}>{val}</Typography>
                                        <Typography sx={{ fontSize: 10, color: 'text.disabled' }}>{lbl}</Typography>
                                    </Box>
                                ))}
                            </Box>
                            <Box sx={{ width: '100%', py: 0.75, border: '1px solid', borderColor: 'divider', borderRadius: 1, bgcolor: 'action.hover', fontSize: 12, textAlign: 'center', cursor: 'pointer', color: 'text.primary' }}>
                                Ver panel completo
                            </Box>
                        </Box>

                        <Box>
                            <Typography sx={{ fontSize: 12, fontWeight: 500, mb: 1 }}>
                                Últimas notificaciones
                            </Typography>
                            
                            {notifications.map((msg) => (
                                <NotificationItem
                                    key={msg.id}
                                    // Usamos el contenido del mensaje como texto principal
                                    text={msg.content ?? 'Sin contenido'} 
                                    // Pasamos el tipo de notificación o la fecha que agregamos al DTO
                                    time={msg.notificationType ?? 'Alerta'} 
                                />
                            ))}
                        </Box>
                    </Box>
                </Box>
                <Outlet />
            </Box>
        </Box>
    );
};

export default AdminDashboard;