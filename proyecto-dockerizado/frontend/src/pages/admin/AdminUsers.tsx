import { useEffect, useState, useCallback } from 'react';
import {
    Alert,
    Box,
    Button,
    CircularProgress,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    FormControl,
    IconButton,
    InputLabel,
    MenuItem,
    Select,
    Stack,
    TextField,
    Typography,
} from '@mui/material';
import { Add, Delete, Edit } from '@mui/icons-material';
import PeopleIcon from '@mui/icons-material/People';
import AdminPanelSettingsIcon from '@mui/icons-material/AdminPanelSettings';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import SportsGymnasticsIcon from '@mui/icons-material/SportsGymnastics';
import StatCard from '../../components/ui/StatCard';
import { getUsers, updateUser, createUser, deleteUser, getRoles } from './services/adminService';

interface UserRow {
    id: number;
    fullName: string;
    email: string;
    roleName: string;
    role?: string;
}

interface RoleRow {
    id: number;
    name: string;
}

type UserForm = {
    fullName: string;
    email: string;
    password: string;
    roleId: string | number;
};

const EMPTY_FORM: UserForm = {
    fullName: '',
    email: '',
    password: '',
    roleId: ''
};

function Users() {
    const [rows, setRows] = useState<UserRow[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [form, setForm] = useState<UserForm>(EMPTY_FORM);
    const [editId, setEditId] = useState<number | null>(null);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [deleteId, setDeleteId] = useState<number | null>(null);
    const [metrics, setMetrics] = useState({ users: 0, admins: 0, trainers: 0, students: 0 });
    const [roles, setRoles] = useState<RoleRow[]>([]);

    const openCreate = () => { setForm(EMPTY_FORM); setEditId(null); setDialogOpen(true); };
    const openEdit = (row: UserRow) => {
        const matchedRole = roles.find(r => r.name === row.roleName);
        setForm({ fullName: row.fullName, email: row.email, password: '', roleId: matchedRole?.id ?? '' });
        setEditId(row.id);
        setDialogOpen(true);
    };
    const openDelete = (id: number) => { setDeleteId(id); setConfirmOpen(true); };

    const load = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);

            const [usersRes, rolesRes] = await Promise.all([
                getUsers(),
                getRoles(),
            ]);

            const users: UserRow[] = Array.isArray(usersRes.data) ? usersRes.data : (usersRes as unknown as UserRow[]);
            setRows(users);
            setMetrics({
                users:    users.length,
                admins:   users.filter(u => u.roleName === 'ROLE_ADMIN').length,
                trainers: users.filter(u => u.roleName === 'ROLE_TRAINER').length,
                students: users.filter(u => u.roleName === 'ROLE_STUDENT').length,
            });

            const rolesData: RoleRow[] = Array.isArray(rolesRes.data) ? rolesRes.data : (rolesRes as unknown as RoleRow[]);
            setRoles(rolesData);

        } catch {
            setError('Error al cargar los usuarios');
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        let active = true;
        load().then(() => { if (!active) return; });
        return () => { active = false; };
    }, [load]);

    const handleSave = async () => {
        try {
            const payload: Partial<UserForm> = {
                fullName: form.fullName,
                email: form.email,
                password: form.password,
                roleId: form.roleId,
            };
            if (editId && !payload.password) delete payload.password;
            if (editId) await updateUser(editId, payload);
            else await createUser(payload);
            setDialogOpen(false);
            await load();
        } catch {
            setError('Error al guardar el usuario');
        }
    };

    const handleDelete = async () => {
        try {
            if (deleteId !== null) await deleteUser(deleteId);
            setConfirmOpen(false);
            await load();
        } catch {
            setError('Error al eliminar el usuario');
        }
    };

    const field = (key: keyof UserForm) => ({
        value: form[key],
        onChange: (e: React.ChangeEvent<HTMLInputElement>) => setForm({ ...form, [key]: e.target.value }),
        fullWidth: true,
    });

    return (
        <Box sx={{ display: 'flex', height: '100vh', bgcolor: 'grey.100' }}>

            <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', overflow: 'hidden', maxHeight: 650,}}>

                {/* Header */}
                <Box sx={{
                    bgcolor: 'background.paper',
                    borderBottom: '1px solid',
                    borderColor: 'divider',
                    px: 2.5, py: 1.5,
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                }}>
                    <Typography sx={{ fontSize: 18, fontWeight: 500 }}>Usuarios</Typography>
                    <Button variant="contained" size="small" startIcon={<Add />} onClick={openCreate}>
                        Nuevo Usuario
                    </Button>
                </Box>

                <Box sx={{ flex: 1, overflowY: 'auto', p: 2, display: 'flex', flexDirection: 'column', gap: 2 }}>

                    {error && (
                        <Alert severity="error" onClose={() => setError(null)}>
                            {error}
                        </Alert>
                    )}

                    {/* Stat Cards */}
                    <Box sx={{ display: 'flex', gap: 1.5 }}>
                        <StatCard label="Total usuarios"   value={metrics.users}    sub="Registrados en el sistema"  icon={<PeopleIcon fontSize="small" />}             iconColor="#4f46e5" />
                        <StatCard label="Administradores"  value={metrics.admins}   sub="Con acceso total"           icon={<AdminPanelSettingsIcon fontSize="small" />}  iconColor="#0f6e56" />
                        <StatCard label="Entrenadores"     value={metrics.trainers} sub="Capacitadores activos"      icon={<FitnessCenterIcon fontSize="small" />}       iconColor="#ba7517" />
                        <StatCard label="Estudiantes"      value={metrics.students} sub="Estudiantes activos"        icon={<SportsGymnasticsIcon fontSize="small" />}    iconColor="#993556" />
                    </Box>

                    {/* Tabla de usuarios */}
                    <Box sx={{
                        bgcolor: 'background.paper',
                        border: '1px solid',
                        borderColor: 'divider',
                        borderRadius: 2,
                        overflow: 'hidden',
                        overflowY: 'auto',
                    }}>
                        {/* Cabecera de la tabla */}
                        <Box sx={{
                            px: 2.5, py: 1.5,
                            borderBottom: '1px solid',
                            borderColor: 'divider',
                            display: 'flex',
                            justifyContent: 'space-between',
                            alignItems: 'center',
                        }}>
                            <Typography sx={{ fontSize: 14, fontWeight: 500 }}>Lista de usuarios</Typography>
                            <Box sx={{
                                fontSize: 10, px: 1, py: 0.25,
                                borderRadius: 99, border: '1px solid',
                                borderColor: 'divider', color: 'text.secondary',
                            }}>
                                {metrics.users} registros
                            </Box>
                        </Box>

                        {/* Encabezado columnas */}
                        <Box sx={{
                            display: 'grid',
                            gridTemplateColumns: '0.5fr 1.5fr 2fr 1fr 1fr 80px',
                            px: 2.5, py: 1,
                            bgcolor: 'action.hover',
                            borderBottom: '1px solid',
                            borderColor: 'divider',
                        }}>
                            {['ID', 'Nombre', 'Email', 'Rol', ''].map((col) => (
                                <Typography key={col} sx={{ fontSize: 11, fontWeight: 500, color: 'text.secondary' }}>
                                    {col}
                                </Typography>
                            ))}
                        </Box>

                        {/* Filas */}
                        {loading ? (
                            <Box sx={{ display: 'flex', justifyContent: 'center', py: 6 }}>
                                <CircularProgress size={28} />
                            </Box>
                        ) : rows.length === 0 ? (
                            <Box sx={{ py: 6, textAlign: 'center' }}>
                                <Typography sx={{ fontSize: 13, color: 'text.disabled' }}>
                                    No hay usuarios registrados
                                </Typography>
                            </Box>
                        ) : (
                            rows.map((row) => (
                                <Box
                                    key={row.id}
                                    sx={{
                                        display: 'grid',
                                        gridTemplateColumns: '0.5fr 1.5fr 2fr 1fr 1fr 80px',
                                        px: 2.5,
                                        py: '12px',
                                        borderBottom: '1px solid',
                                        borderColor: 'divider',
                                        alignItems: 'center',
                                        '&:last-child': { borderBottom: 'none' },
                                        '&:hover': { bgcolor: 'action.hover' },
                                    }}
                                >
                                    <Typography sx={{ fontSize: 12, color: 'text.disabled' }}>
                                        {row.id}
                                    </Typography>

                                    <Box sx={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                                        <Typography sx={{ fontSize: 12, fontWeight: 500, color: 'text.primary' }}>
                                            {row.fullName}
                                        </Typography>
                                    </Box>

                                    <Typography sx={{ fontSize: 12, color: 'text.secondary' }}>
                                        {row.email}
                                    </Typography>

                                    <Box sx={{
                                        display: 'inline-flex', alignItems: 'center',
                                        px: 1, py: 0.25, borderRadius: 99,
                                        bgcolor: row.role === 'ADMIN' ? '#eef2ff' : '#f3f4f6',
                                        width: 'fit-content',
                                    }}>
                                        <Typography sx={{
                                            fontSize: 10, fontWeight: 500,
                                            color: row.roleName === 'ROLE_ADMIN' ? '#4f46e5'
                                                : row.roleName === 'ROLE_TRAINER' ? '#ba7517'
                                                : row.roleName === 'ROLE_STUDENT' ? '#993556'
                                                : 'text.secondary',
                                        }}>
                                            {row.roleName === 'ROLE_ADMIN' ? 'Administrador'
                                                : row.roleName === 'ROLE_TRAINER' ? 'Entrenador'
                                                : row.roleName === 'ROLE_STUDENT' ? 'Estudiante'
                                                : 'Sin rol'}
                                        </Typography>
                                    </Box>

                                    <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: '4px' }}>
                                        <IconButton size="small" onClick={() => openEdit(row)}>
                                            <Edit sx={{ fontSize: 15 }} />
                                        </IconButton>
                                        <IconButton size="small" color="error" onClick={() => openDelete(row.id)}>
                                            <Delete sx={{ fontSize: 15 }} />
                                        </IconButton>
                                    </Box>
                                </Box>
                            ))
                        )}
                    </Box>
                </Box>
            </Box>

            {/* Dialog crear / editar */}
            <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
                <DialogTitle sx={{ fontSize: 15, fontWeight: 500 }}>
                    {editId ? 'Editar Usuario' : 'Nuevo Usuario'}
                </DialogTitle>
                <DialogContent>
                    <Stack spacing={2} sx={{ mt: 1 }}>
                        <TextField label="Nombre" {...field('fullName')} size="small" />
                        <TextField label="Email" {...field('email')} type="email" size="small" />
                        <TextField
                            label={editId ? 'Nueva contraseña (vacío = no cambiar)' : 'Contraseña'}
                            {...field('password')}
                            type="password"
                            size="small"
                        />
                        <FormControl fullWidth size="small">
                            <InputLabel>Rol</InputLabel>
                            <Select value={form.roleId} label="Rol" onChange={(e) => setForm({ ...form, roleId: e.target.value })}>
                                {roles.map(r => (
                                    <MenuItem key={r.id} value={r.id}>
                                        {r.name === 'ROLE_ADMIN' ? 'Admin'
                                            : r.name === 'ROLE_TRAINER' ? 'Entrenador'
                                            : r.name === 'ROLE_STUDENT' ? 'Estudiante'
                                            : r.name}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Stack>
                </DialogContent>
                <DialogActions>
                    <Button size="small" onClick={() => setDialogOpen(false)}>Cancelar</Button>
                    <Button size="small" variant="contained" onClick={handleSave}>Guardar</Button>
                </DialogActions>
            </Dialog>

            {/* Dialog confirmar eliminación */}
            <Dialog open={confirmOpen} onClose={() => setConfirmOpen(false)}>
                <DialogTitle sx={{ fontSize: 15, fontWeight: 500 }}>Confirmar eliminación</DialogTitle>
                <DialogContent>
                    <Typography sx={{ fontSize: 13 }}>
                        ¿Estás seguro de que deseas eliminar este usuario?
                    </Typography>
                </DialogContent>
                <DialogActions>
                    <Button size="small" onClick={() => setConfirmOpen(false)}>Cancelar</Button>
                    <Button size="small" variant="contained" color="error" onClick={handleDelete}>
                        Eliminar
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
}

export default Users;
