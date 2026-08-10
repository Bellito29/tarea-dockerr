import { useEffect, useState, useCallback } from 'react';
import {
    Alert, Box, Button, CircularProgress, Dialog, DialogActions,
    DialogContent, DialogTitle, IconButton, Stack, TextField, Typography,
    Chip, FormControl, InputLabel, Select, MenuItem, OutlinedInput
} from '@mui/material';
import { Add, Delete, Edit } from '@mui/icons-material';
import SecurityIcon from '@mui/icons-material/Security';
import KeyIcon from '@mui/icons-material/Key';
import StatCard from '../../components/ui/StatCard';
import { getRoles, createRole, updateRole, deleteRole, getPermissions } from './services/adminService';

interface Permission {
    id: number;
    name: string;
}

interface RoleRow {
    id: number;
    name: string;
    description?: string;
    permissions?: Permission[];
}

type RoleForm = {
    name: string;
    description: string;
    permissionIds: number[];
};

const EMPTY_FORM: RoleForm = { name: '', description: '', permissionIds: [] };

function Roles() {
    const [rows, setRows] = useState<RoleRow[]>([]);
    const [permissions, setPermissions] = useState<Permission[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [form, setForm] = useState<RoleForm>(EMPTY_FORM);
    const [editId, setEditId] = useState<number | null>(null);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [deleteId, setDeleteId] = useState<number | null>(null);

    const openCreate = () => { setForm(EMPTY_FORM); setEditId(null); setDialogOpen(true); };
    const openEdit = (row: RoleRow) => {
        setForm({
            name: row.name,
            description: row.description ?? '',
            permissionIds: row.permissions?.map(p => p.id) ?? [],
        });
        setEditId(row.id);
        setDialogOpen(true);
    };
    const openDelete = (id: number) => { setDeleteId(id); setConfirmOpen(true); };

    const load = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            const [rolesRes, permsRes] = await Promise.all([getRoles(), getPermissions()]);
            const roles: RoleRow[] = Array.isArray(rolesRes.data) ? rolesRes.data : (rolesRes as unknown as RoleRow[]);
            const perms: Permission[] = Array.isArray(permsRes.data) ? permsRes.data : (permsRes as unknown as Permission[]);
            setRows(roles);
            setPermissions(perms);
        } catch {
            setError('Error al cargar los roles');
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
            const payload = {
                name: form.name,
                description: form.description,
                permissionIds: form.permissionIds,
            };
            if (editId) await updateRole(editId, payload);
            else await createRole(payload);
            setDialogOpen(false);
            await load();
        } catch {
            setError('Error al guardar el rol');
        }
    };

    const handleDelete = async () => {
        try {
            if (deleteId !== null) await deleteRole(deleteId);
            setConfirmOpen(false);
            await load();
        } catch {
            setError('Error al eliminar el rol');
        }
    };

    return (
        <Box sx={{ display: 'flex', height: '100vh', bgcolor: 'grey.100' }}>
            <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', overflow: 'hidden', maxHeight: 650 }}>

                {/* Header */}
                <Box sx={{
                    bgcolor: 'background.paper', borderBottom: '1px solid',
                    borderColor: 'divider', px: 2.5, py: 1.5,
                    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
                }}>
                    <Typography sx={{ fontSize: 18, fontWeight: 500 }}>Roles</Typography>
                    <Button variant="contained" size="small" startIcon={<Add />} onClick={openCreate}>
                        Nuevo Rol
                    </Button>
                </Box>

                <Box sx={{ flex: 1, overflowY: 'auto', p: 2, display: 'flex', flexDirection: 'column', gap: 2 }}>

                    {error && <Alert severity="error" onClose={() => setError(null)}>{error}</Alert>}

                    {/* Stat Cards */}
                    <Box sx={{ display: 'flex', gap: 1.5 }}>
                        <StatCard label="Total roles" value={rows.length} sub="Roles registrados"
                            icon={<SecurityIcon fontSize="small" />} iconColor="#4f46e5" />
                        <StatCard label="Permisos disponibles" value={permissions.length} sub="Permisos en el sistema"
                            icon={<KeyIcon fontSize="small" />} iconColor="#0f6e56" />
                    </Box>

                    {/* Tabla */}
                    <Box sx={{
                        bgcolor: 'background.paper', border: '1px solid',
                        borderColor: 'divider', borderRadius: 2, overflow: 'hidden', overflowY: 'auto',
                    }}>
                        {/* Cabecera tabla */}
                        <Box sx={{
                            px: 2.5, py: 1.5, borderBottom: '1px solid', borderColor: 'divider',
                            display: 'flex', justifyContent: 'space-between', alignItems: 'center',
                        }}>
                            <Typography sx={{ fontSize: 14, fontWeight: 500 }}>Lista de roles</Typography>
                            <Box sx={{
                                fontSize: 10, px: 1, py: 0.25, borderRadius: 99,
                                border: '1px solid', borderColor: 'divider', color: 'text.secondary',
                            }}>
                                {rows.length} registros
                            </Box>
                        </Box>

                        {/* Encabezado columnas */}
                        <Box sx={{
                            display: 'grid', gridTemplateColumns: '0.5fr 1.5fr 2fr 3fr 80px',
                            px: 2.5, py: 1, bgcolor: 'action.hover',
                            borderBottom: '1px solid', borderColor: 'divider',
                        }}>
                            {['ID', 'Nombre', 'Descripción', 'Permisos', ''].map((col) => (
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
                                    No hay roles registrados
                                </Typography>
                            </Box>
                        ) : (
                            rows.map((row) => (
                                <Box key={row.id} sx={{
                                    display: 'grid', gridTemplateColumns: '0.5fr 1.5fr 2fr 3fr 80px',
                                    px: 2.5, py: '12px', borderBottom: '1px solid', borderColor: 'divider',
                                    alignItems: 'center',
                                    '&:last-child': { borderBottom: 'none' },
                                    '&:hover': { bgcolor: 'action.hover' },
                                }}>
                                    <Typography sx={{ fontSize: 12, color: 'text.disabled' }}>{row.id}</Typography>

                                    <Box sx={{
                                        display: 'inline-flex', alignItems: 'center',
                                        px: 1, py: 0.25, borderRadius: 99, bgcolor: '#eef2ff', width: 'fit-content',
                                    }}>
                                        <Typography sx={{ fontSize: 10, fontWeight: 500, color: '#4f46e5' }}>
                                            {row.name}
                                        </Typography>
                                    </Box>

                                    <Typography sx={{ fontSize: 12, color: 'text.secondary' }}>
                                        {row.description ?? '—'}
                                    </Typography>

                                    <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                                        {(row.permissions?.length ?? 0) > 0
                                            ? row.permissions?.map(p => (
                                                <Chip key={p.id} label={p.name} size="small"
                                                    sx={{ fontSize: 9, height: 18 }} />
                                            ))
                                            : <Typography sx={{ fontSize: 11, color: 'text.disabled' }}>
                                                {row.permissions?.length ?? 0}
                                            </Typography>
                                        }
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
                    {editId ? 'Editar Rol' : 'Nuevo Rol'}
                </DialogTitle>
                <DialogContent>
                    <Stack spacing={2} sx={{ mt: 1 }}>
                        <TextField label="Nombre" value={form.name} size="small" fullWidth
                            onChange={(e) => setForm({ ...form, name: e.target.value })} />
                        <TextField label="Descripción" value={form.description} size="small" fullWidth
                            onChange={(e) => setForm({ ...form, description: e.target.value })} />
                        <FormControl fullWidth size="small">
                            <InputLabel>Permisos</InputLabel>
                            <Select
                                multiple
                                value={form.permissionIds}
                                onChange={(e) => setForm({ ...form, permissionIds: e.target.value as number[] })}
                                input={<OutlinedInput label="Permisos" />}
                                renderValue={(selected) => (
                                    <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                                        {(selected as number[]).map(id => {
                                            const perm = permissions.find(p => p.id === id);
                                            return <Chip key={id} label={perm?.name ?? id} size="small" />;
                                        })}
                                    </Box>
                                )}
                            >
                                {permissions.map(p => (
                                    <MenuItem key={p.id} value={p.id}>{p.name}</MenuItem>
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
                        ¿Estás seguro de que deseas eliminar este rol?
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

export default Roles;
