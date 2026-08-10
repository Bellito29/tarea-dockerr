import { useEffect, useState, ReactNode } from 'react';
import {
    Alert,
    Box,
    Button,
    Card,
    CardContent,
    Chip,
    CircularProgress,
    Divider,
    Grid,
    Paper,
    Stack,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    TextField,
    Typography,
} from '@mui/material';

import {
    FitnessCenter,
    DirectionsRun,
    Schedule,
    BarChart,
    Event,
    Message,
    Notifications,
    Recommend,
    Send,
} from '@mui/icons-material';

import StudentLayout from './StudentLayout';

import {
    createMessage,
    getEvents,
    getExercises,
    getMessages,
    getNotifications,
    getRecomendations,
    getRoutines,
    getSesions,
    getStats,
} from './services/student.service';

interface DashboardData {
    routines: Record<string, unknown>[];
    exercises: Record<string, unknown>[];
    sesions: Record<string, unknown>[];
    stats: Record<string, unknown>[];
    events: Record<string, unknown>[];
    messages: Record<string, unknown>[];
    notifications: Record<string, unknown>[];
    recomendations: Record<string, unknown>[];
}

const EMPTY_DATA: DashboardData = {
    routines: [],
    exercises: [],
    sesions: [],
    stats: [],
    events: [],
    messages: [],
    notifications: [],
    recomendations: [],
};

function formatDate(value: unknown): string {
    if (!value) return '-';

    try {
        return new Date(value as string).toLocaleString('es-CO', {
            dateStyle: 'medium',
            timeStyle: 'short',
        });
    } catch {
        return String(value);
    }
}

function formatTime(seconds: number | null | undefined): string {
    if (!seconds && seconds !== 0) return '-';

    const totalSeconds = Number(seconds);
    const hours = Math.floor(totalSeconds / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const remainingSeconds = totalSeconds % 60;

    return `${hours}h ${minutes}m ${remainingSeconds}s`;
}

function Section({ id, title, subtitle, children }: { id: string; title: string; subtitle?: string; children: ReactNode }) {
    return (
        <Paper
            id={id}
            elevation={0}
            sx={{
                p: 2.5,
                borderRadius: 3,
                border: '1px solid',
                borderColor: 'divider',
                scrollMarginTop: 90,
            }}
        >
            <Box sx={{ mb: 2 }}>
                <Typography variant="h6" sx={{ fontWeight: 700 }}>
                    {title}
                </Typography>

                {subtitle && (
                    <Typography variant="body2" color="text.secondary">
                        {subtitle}
                    </Typography>
                )}
            </Box>

            {children}
        </Paper>
    );
}

function SummaryCard({ title, value, description, icon, color }: { title: string; value: number; description: string; icon: ReactNode; color: string }) {
    return (
        <Card
            elevation={0}
            sx={{
                height: '100%',
                borderRadius: 3,
                border: '1px solid',
                borderColor: 'divider',
            }}
        >
            <CardContent>
                <Stack direction="row" sx={{ justifyContent: 'space-between', alignItems: 'center' }}>
                    <Box>
                        <Typography variant="body2" color="text.secondary">
                            {title}
                        </Typography>

                        <Typography variant="h4" sx={{ fontWeight: 800 }}>
                            {value}
                        </Typography>

                        <Typography variant="caption" color="text.secondary">
                            {description}
                        </Typography>
                    </Box>

                    <Box
                        sx={{
                            width: 44,
                            height: 44,
                            borderRadius: 2,
                            bgcolor: color,
                            color: '#fff',
                            display: 'grid',
                            placeItems: 'center',
                        }}
                    >
                        {icon}
                    </Box>
                </Stack>
            </CardContent>
        </Card>
    );
}

function StudentDashboard() {
    const [data, setData] = useState<DashboardData>(EMPTY_DATA);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [messageContent, setMessageContent] = useState('');
    const [sending, setSending] = useState(false);

    const loadData = async () => {
        try {
            setLoading(true);
            setError(null);

            const [
                routines,
                exercises,
                sesions,
                stats,
                events,
                messages,
                notifications,
                recomendations,
            ] = await Promise.all([
                getRoutines(),
                getExercises(),
                getSesions(),
                getStats(),
                getEvents(),
                getMessages(),
                getNotifications(),
                getRecomendations(),
            ]);

            setData({
                routines,
                exercises,
                sesions,
                stats,
                events,
                messages,
                notifications,
                recomendations,
            });
        } catch {
            setError('No se pudieron cargar los datos del estudiante. Revisa el token JWT, la URL del API y los permisos del backend.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadData();
    }, []);

    const handleCreateMessage = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (!messageContent.trim()) {
            setError('El mensaje no puede estar vacío.');
            return;
        }

        try {
            setSending(true);
            setError(null);

            await createMessage({ content: messageContent.trim() });

            setMessageContent('');
            const messages = await getMessages();

            setData((prev) => ({
                ...prev,
                messages,
            }));
        } catch {
            setError('No se pudo enviar el mensaje.');
        } finally {
            setSending(false);
        }
    };

    return (
        <StudentLayout>
            <Stack spacing={3}>
                <Box>
                    <Typography variant="h4" sx={{ fontWeight: 800 }}>
                        Student Dashboard
                    </Typography>

                    <Typography variant="body1" color="text.secondary">
                        Vista de solo lectura para rutinas, ejercicios, sesiones, estadísticas, eventos, notificaciones y recomendaciones. Interacción permitida con mensajes.
                    </Typography>
                </Box>

                {error && (
                    <Alert severity="error" onClose={() => setError(null)}>
                        {error}
                    </Alert>
                )}

                {loading ? (
                    <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
                        <CircularProgress />
                    </Box>
                ) : (
                    <>
                        <Box id="summary" sx={{ scrollMarginTop: 90 }}>
                            <Grid container spacing={2}>
                                <Grid size={{ xs: 12, sm: 6, md: 3 }}>
                                    <SummaryCard
                                        title="Rutinas"
                                        value={data.routines.length}
                                        description="Solo lectura"
                                        icon={<FitnessCenter />}
                                        color="#16a34a"
                                    />
                                </Grid>

                                <Grid size={{ xs: 12, sm: 6, md: 3 }}>
                                    <SummaryCard
                                        title="Ejercicios"
                                        value={data.exercises.length}
                                        description="Catálogo disponible"
                                        icon={<DirectionsRun />}
                                        color="#2563eb"
                                    />
                                </Grid>

                                <Grid size={{ xs: 12, sm: 6, md: 3 }}>
                                    <SummaryCard
                                        title="Sesiones"
                                        value={data.sesions.length}
                                        description="Sesiones asignadas"
                                        icon={<Schedule />}
                                        color="#9333ea"
                                    />
                                </Grid>

                                <Grid size={{ xs: 12, sm: 6, md: 3 }}>
                                    <SummaryCard
                                        title="Mensajes"
                                        value={data.messages.length}
                                        description="GET y POST habilitado"
                                        icon={<Message />}
                                        color="#ea580c"
                                    />
                                </Grid>
                            </Grid>
                        </Box>

                        <Section
                            id="routines"
                            title="Rutinas"
                            subtitle="Endpoint: GET /rest/routines — el estudiante solo consulta sus rutinas disponibles."
                        >
                            <TableContainer>
                                <Table size="small">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>ID</TableCell>
                                            <TableCell>Nombre</TableCell>
                                            <TableCell>Descripción</TableCell>
                                            <TableCell>Visibilidad</TableCell>
                                        </TableRow>
                                    </TableHead>

                                    <TableBody>
                                        {data.routines.map((routine) => (
                                            <TableRow key={routine.id as string} hover>
                                                <TableCell>{String(routine.id ?? '')}</TableCell>
                                                <TableCell>{String(routine.routineName ?? '')}</TableCell>
                                                <TableCell>{String(routine.description ?? '')}</TableCell>
                                                <TableCell>
                                                    <Chip
                                                        size="small"
                                                        label={String(routine.visibility ?? 'Sin dato')}
                                                        color="success"
                                                        variant="outlined"
                                                    />
                                                </TableCell>
                                            </TableRow>
                                        ))}

                                        {data.routines.length === 0 && (
                                            <TableRow>
                                                <TableCell colSpan={4} align="center">
                                                    No hay rutinas disponibles.
                                                </TableCell>
                                            </TableRow>
                                        )}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </Section>

                        <Section
                            id="exercises"
                            title="Ejercicios"
                            subtitle="Endpoint: GET /rest/exercises — catálogo de ejercicios en modo solo lectura."
                        >
                            <TableContainer>
                                <Table size="small">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>ID</TableCell>
                                            <TableCell>Nombre</TableCell>
                                            <TableCell>Tipo</TableCell>
                                            <TableCell>Duración</TableCell>
                                            <TableCell>Dificultad</TableCell>
                                            <TableCell>Descripción</TableCell>
                                        </TableRow>
                                    </TableHead>

                                    <TableBody>
                                        {data.exercises.map((exercise) => (
                                            <TableRow key={exercise.id as string} hover>
                                                <TableCell>{String(exercise.id ?? '')}</TableCell>
                                                <TableCell>{String(exercise.name ?? '')}</TableCell>
                                                <TableCell>{String(exercise.type ?? '')}</TableCell>
                                                <TableCell>{String(exercise.duration ?? '')} min</TableCell>
                                                <TableCell>{String(exercise.difficulty ?? '')}</TableCell>
                                                <TableCell>{String(exercise.description ?? '')}</TableCell>
                                            </TableRow>
                                        ))}

                                        {data.exercises.length === 0 && (
                                            <TableRow>
                                                <TableCell colSpan={6} align="center">
                                                    No hay ejercicios disponibles.
                                                </TableCell>
                                            </TableRow>
                                        )}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </Section>

                        <Section
                            id="sesions"
                            title="Sesiones"
                            subtitle="Endpoint: GET /rest/sesions — el estudiante consulta sus sesiones."
                        >
                            <TableContainer>
                                <Table size="small">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>ID</TableCell>
                                            <TableCell>Fecha</TableCell>
                                            <TableCell>Duración</TableCell>
                                            <TableCell>Usuario</TableCell>
                                            <TableCell>Rutina</TableCell>
                                            <TableCell>Ejercicios</TableCell>
                                        </TableRow>
                                    </TableHead>

                                    <TableBody>
                                        {data.sesions.map((sesion) => (
                                            <TableRow key={sesion.id as string} hover>
                                                <TableCell>{String(sesion.id ?? '')}</TableCell>
                                                <TableCell>{formatDate(sesion.sesionDate)}</TableCell>
                                                <TableCell>{sesion.duration ? `${String(sesion.duration)} min` : '-'}</TableCell>
                                                <TableCell>{String(sesion.userName ?? '-')}</TableCell>
                                                <TableCell>{String(sesion.routineName ?? '-')}</TableCell>
                                                <TableCell>
                                                    {Array.isArray(sesion.exercises) && sesion.exercises.length > 0
                                                        ? sesion.exercises.join(', ')
                                                        : '-'}
                                                </TableCell>
                                            </TableRow>
                                        ))}

                                        {data.sesions.length === 0 && (
                                            <TableRow>
                                                <TableCell colSpan={6} align="center">
                                                    No hay sesiones registradas.
                                                </TableCell>
                                            </TableRow>
                                        )}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </Section>

                        <Section
                            id="stats"
                            title="Estadísticas"
                            subtitle="Endpoint: GET /rest/stats — consulta de estadísticas personales."
                        >
                            <Grid container spacing={2}>
                                {data.stats.map((stat) => (
                                    <Grid size={{ xs: 12, md: 4 }} key={stat.id as string}>
                                        <Card variant="outlined" sx={{ borderRadius: 3 }}>
                                            <CardContent>
                                                <Stack direction="row" spacing={1} sx={{ alignItems: 'center', mb: 1 }}>
                                                    <BarChart color="primary" />
                                                    <Typography sx={{ fontWeight: 700 }}>
                                                        Estadística #{String(stat.id ?? '')}
                                                    </Typography>
                                                </Stack>

                                                <Typography variant="body2" color="text.secondary">
                                                    Tiempo: {formatTime(stat.time as number | null | undefined)}
                                                </Typography>

                                                <Typography variant="body2" color="text.secondary">
                                                    Ejercicios realizados: {String(stat.ammountExercises ?? '')}
                                                </Typography>
                                            </CardContent>
                                        </Card>
                                    </Grid>
                                ))}

                                {data.stats.length === 0 && (
                                    <Grid size={12}>
                                        <Typography align="center" color="text.secondary">
                                            No hay estadísticas registradas.
                                        </Typography>
                                    </Grid>
                                )}
                            </Grid>
                        </Section>

                        <Section
                            id="events"
                            title="Eventos"
                            subtitle="Endpoint: GET /rest/events — eventos disponibles para el estudiante."
                        >
                            <TableContainer>
                                <Table size="small">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>ID</TableCell>
                                            <TableCell>Título</TableCell>
                                            <TableCell>Lugar</TableCell>
                                            <TableCell>Fecha</TableCell>
                                        </TableRow>
                                    </TableHead>

                                    <TableBody>
                                        {data.events.map((event) => (
                                            <TableRow key={event.id as string} hover>
                                                <TableCell>{String(event.id ?? '')}</TableCell>
                                                <TableCell>{String(event.title ?? '')}</TableCell>
                                                <TableCell>{String(event.place ?? '')}</TableCell>
                                                <TableCell>{formatDate(event.date)}</TableCell>
                                            </TableRow>
                                        ))}

                                        {data.events.length === 0 && (
                                            <TableRow>
                                                <TableCell colSpan={4} align="center">
                                                    No hay eventos disponibles.
                                                </TableCell>
                                            </TableRow>
                                        )}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </Section>

                        <Section
                            id="messages"
                            title="Mensajes"
                            subtitle="Endpoint: GET /rest/messages y POST /rest/messages — aquí sí hay interacción del estudiante."
                        >
                            <Stack
                                component="form"
                                onSubmit={handleCreateMessage}
                                direction={{ xs: 'column', md: 'row' }}
                                spacing={2}
                                sx={{ mb: 2 }}
                            >
                                <TextField
                                    label="Escribe un mensaje"
                                    value={messageContent}
                                    onChange={(event) => setMessageContent(event.target.value)}
                                    fullWidth
                                    multiline
                                    minRows={1}
                                />

                                <Button
                                    type="submit"
                                    variant="contained"
                                    startIcon={<Send />}
                                    disabled={sending}
                                    sx={{ minWidth: 160 }}
                                >
                                    {sending ? 'Enviando...' : 'Enviar'}
                                </Button>
                            </Stack>

                            <Divider sx={{ mb: 2 }} />

                            <Stack spacing={1.5}>
                                {data.messages.map((message) => (
                                    <Paper
                                        key={message.id as string}
                                        variant="outlined"
                                        sx={{
                                            p: 1.5,
                                            borderRadius: 2,
                                            bgcolor: 'grey.50',
                                        }}
                                    >
                                        <Typography variant="body2" color="text.secondary">
                                            Mensaje #{String(message.id ?? '')}
                                        </Typography>

                                        <Typography>{String(message.content ?? '')}</Typography>
                                    </Paper>
                                ))}

                                {data.messages.length === 0 && (
                                    <Typography align="center" color="text.secondary">
                                        No hay mensajes registrados.
                                    </Typography>
                                )}
                            </Stack>
                        </Section>

                        <Section
                            id="notifications"
                            title="Notificaciones"
                            subtitle="Endpoint: GET /rest/notifications — el estudiante consulta sus notificaciones."
                        >
                            <Stack spacing={1.5}>
                                {data.notifications.map((notification) => (
                                    <Paper
                                        key={notification.id as string}
                                        variant="outlined"
                                        sx={{ p: 1.5, borderRadius: 2 }}
                                    >
                                        <Stack direction="row" sx={{ justifyContent: 'space-between', alignItems: 'center' }}>
                                            <Typography sx={{ fontWeight: 700 }}>
                                                {String(notification.message ?? 'Notificación sin mensaje')}
                                            </Typography>

                                            <Chip
                                                size="small"
                                                label={String(notification.type ?? 'Tipo no definido')}
                                                color="info"
                                                variant="outlined"
                                            />
                                        </Stack>

                                        <Typography variant="caption" color="text.secondary">
                                            Referencia: {String(notification.referenceId ?? '-')}
                                        </Typography>
                                    </Paper>
                                ))}

                                {data.notifications.length === 0 && (
                                    <Typography align="center" color="text.secondary">
                                        No hay notificaciones registradas.
                                    </Typography>
                                )}
                            </Stack>
                        </Section>

                        <Section
                            id="recomendations"
                            title="Recomendaciones"
                            subtitle="Endpoint: GET /rest/recomendations — recomendaciones en modo solo lectura."
                        >
                            <Stack spacing={1.5}>
                                {data.recomendations.map((recomendation) => (
                                    <Paper
                                        key={recomendation.id as string}
                                        variant="outlined"
                                        sx={{
                                            p: 1.5,
                                            borderRadius: 2,
                                            bgcolor: '#f8fafc',
                                        }}
                                    >
                                        <Stack direction="row" spacing={1} sx={{ alignItems: 'center' }}>
                                            <Recommend color="success" />

                                            <Typography>
                                                {String(recomendation.description ?? '')}
                                            </Typography>
                                        </Stack>
                                    </Paper>
                                ))}

                                {data.recomendations.length === 0 && (
                                    <Typography align="center" color="text.secondary">
                                        No hay recomendaciones registradas.
                                    </Typography>
                                )}
                            </Stack>
                        </Section>
                    </>
                )}
            </Stack>
        </StudentLayout>
    );
}

export default StudentDashboard;