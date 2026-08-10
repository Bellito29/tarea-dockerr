import { useEffect, useState, useCallback } from 'react';
import {
    Alert, Box, Button, CircularProgress, Dialog, DialogActions,
    DialogContent, DialogTitle, IconButton, Stack, TextField, Typography,
    Chip
} from '@mui/material';
import { Add, Delete, Edit } from '@mui/icons-material';
import KeyIcon from '@mui/icons-material/Key';
import CategoryIcon from '@mui/icons-material/Category';
import StatCard from '../../components/ui/StatCard';
import { getPermissions, createPermission, updatePermission, deletePermission } from './services/adminService';

interface PermissionRow {
    id: number;
    name: string;
    description?: string;
}

type PermissionForm = {
    name: string;
    description: string;
};

const EMPTY_FORM: PermissionForm = { name: '', description: '' };

function groupByModule(permissions: PermissionRow[]): Record<string, PermissionRow[]> {
    return permissions.reduce<Record<string, PermissionRow[]>>((acc, p) => {
        const module = p.name?.split(':')[0] ?? 'general';
        if (!acc[module]) acc[module] = [];
        acc[module].push(p);
        return acc;
    }, {});
}

function Permissions() {
    const [rows, setRows] = useState<PermissionRow[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [form, setForm] = useState<PermissionForm>(EMPTY_FORM);
    const [editId, setEditId] = useState<number | null>(null);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [deleteId, setDeleteId] = useState<number | null>(null);

    const openCreate = () => { setForm(EMPTY_FORM); setEditId(null); setDialogOpen(true); };
    const openEdit = (row: PermissionRow) => {
        setForm({ name: row.name, description: row.description ?? '' });
        setEditId(row.id);
        setDialogOpen(true);
    };
    const openDelete = (id: number) => { setDeleteId(id); setConfirmOpen(true); };

    const load = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            const res = await getPermissions();
            const perms: PermissionRow[] = Array.isArray(res.data) ? res.data : (res as unknown as PermissionRow[]);
            setRows(perms);
        } catch {
            setError('Error al cargar los permisos');
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
            const payload = { name: form.name, description: form.description };
            if (editId) await updatePermission(editId, payload);
            else await createPermission(payload);
            setDialogOpen(false);
            await load();
        } catch {
            setError('Error al guardar el permiso');
        }
    };

    const handleDelete = async () => {
        try {
            if (deleteId !== null) await deletePermission(deleteId);
            setConfirmOpen(false);
            await load();
        } catch {
            setError('Error al eliminar el permiso');
        }
    };

    const modules = groupByModule(rows);

    return (
        <Box sx={{ display: 'flex', height: '100vh', bgcolor: 'grey.100' }}>
            <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', overflow: 'hidden', maxHeight: 650 }}>

                {/* Header */}
                <Box sx={{
                    bgcolor: 'background.paper', borderBottom: '1px solid',
                    borderColor: 'divider', px: 2.5, py: 1.5,
                    display: 'flex', justifyContent: 'space-between', alignItems: 'center',
                }}>
                    <Typography sx={{ fontSize: 18, fontWeight: 500 }}>Permisos</Typography>
                    <Button variant="contained" size="small" startIcon={<Add />} onClick={openCreate}>
                        Nuevo Permiso
                    </Button>
                </Box>

                <Box sx={{ flex: 1, overflowY: 'auto', p: 2, display: 'flex', flexDirection: 'column', gap: 2 }}>

                    {error && <Alert severity="error" onClose={() => setError(null)}>{error}</Alert>}

                    {/* Stat Cards */}
                    <Box sx={{ display: 'flex', gap: 1.5 }}>
                        <StatCard
                            label="Total permisos"
                            value={rows.length}
                            sub="Permisos registrados"
                            icon={<KeyIcon fontSize="small" />}
                            iconColor="#0f6e56"
                        />
                        <StatCard
                            label="Módulos"
                            value={Object.keys(modules).length}
                            sub="Grupos de permisos"
                            icon={<CategoryIcon fontSize="small" />}
                            iconColor="#b45309"
                        />
                    </Box>

                    {/* Table */}
                    <Box sx={{
                        bgcolor: 'background.paper', border: '1px solid',
                        borderColor: 'divider', borderRadius: 2, overflow: 'hidden', overflowY: 'auto',
                    }}>
                        {/* Table header bar */}
                        <Box sx={{
                            px: 2.5, py: 1.5, borderBottom: '1px solid', borderColor: 'divider',
                            display: 'flex', justifyContent: 'space-between', alignItems: 'center',
                        }}>
                            <Typography sx={{ fontSize: 14, fontWeight: 500 }}>Lista de permisos</Typography>
                            <Box sx={{
                                fontSize: 10, px: 1, py: 0.25, borderRadius: 99,
                                border: '1px solid', borderColor: 'divider', color: 'text.secondary',
                            }}>
                                {rows.length} registros
                            </Box>
                        </Box>

                        {/* Column headers */}
                        <Box sx={{
                            display: 'grid', gridTemplateColumns: '0.5fr 2fr 3fr 1.5fr 80px',
                            px: 2.5, py: 1, bgcolor: 'action.hover',
                            borderBottom: '1px solid', borderColor: 'divider',
                        }}>
                            {['ID', 'Nombre', 'Descripción', 'Módulo', ''].map((col) => (
                                <Typography key={col} sx={{ fontSize: 11, fontWeight: 500, color: 'text.secondary' }}>
                                    {col}
                                </Typography>
                            ))}
                        </Box>

                        {/* Rows */}
                        {loading ? (
                            <Box sx={{ display: 'flex', justifyContent: 'center', py: 6 }}>
                                <CircularProgress size={28} />
                            </Box>
                        ) : rows.length === 0 ? (
                            <Box sx={{ py: 6, textAlign: 'center' }}>
                                <Typography sx={{ fontSize: 13, color: 'text.disabled' }}>
                                    No hay permisos registrados
                                </Typography>
                            </Box>
                        ) : (
                            rows.map((row) => {
                                const module = row.name?.split(':')[0] ?? 'general';
                                return (
                                    <Box key={row.id} sx={{
                                        display: 'grid', gridTemplateColumns: '0.5fr 2fr 3fr 1.5fr 80px',
                                        px: 2.5, py: '12px', borderBottom: '1px solid', borderColor: 'divider',
                                        alignItems: 'center',
                                        '&:last-child': { borderBottom: 'none' },
                                        '&:hover': { bgcolor: 'action.hover' },
                                    }}>
                                        <Typography sx={{ fontSize: 12, color: 'text.disabled' }}>{row.id}</Typography>

                                        <Box sx={{
                                            display: 'inline-flex', alignItems: 'center',
                                            px: 1, py: 0.25, borderRadius: 99, bgcolor: '#f0fdf4', width: 'fit-content',
                                        }}>
                                            <Typography sx={{ fontSize: 10, fontWeight: 500, color: '#0f6e56' }}>
                                                {row.name}
                                            </Typography>
                                        </Box>

                                        <Typography sx={{ fontSize: 12, color: 'text.secondary' }}>
                                            {row.description ?? '—'}
                                        </Typography>

                                        <Chip
                                            label={module}
                                            size="small"
                                            sx={{ fontSize: 9, height: 18, bgcolor: '#fef3c7', color: '#b45309', border: 'none' }}
                                        />

                                        <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: '4px' }}>
                                            <IconButton size="small" onClick={() => openEdit(row)}>
                                                <Edit sx={{ fontSize: 15 }} />
                                            </IconButton>
                                            <IconButton size="small" color="error" onClick={() => openDelete(row.id)}>
                                                <Delete sx={{ fontSize: 15 }} />
                                            </IconButton>
                                        </Box>
                                    </Box>
                                );
                            })
                        )}
                    </Box>
                </Box>
            </Box>

            {/* Create / Edit Dialog */}
            <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
                <DialogTitle sx={{ fontSize: 15, fontWeight: 500 }}>
                    {editId ? 'Editar Permiso' : 'Nuevo Permiso'}
                </DialogTitle>
                <DialogContent>
                    <Stack spacing={2} sx={{ mt: 1 }}>
                        <TextField
                            label="Nombre"
                            value={form.name}
                            size="small"
                            fullWidth
                            placeholder="ej: users:read"
                            helperText="Usa el formato módulo:acción (ej: users:read, roles:write)"
                            onChange={(e) => setForm({ ...form, name: e.target.value })}
                        />
                        <TextField
                            label="Descripción"
                            value={form.description}
                            size="small"
                            fullWidth
                            onChange={(e) => setForm({ ...form, description: e.target.value })}
                        />
                    </Stack>
                </DialogContent>
                <DialogActions>
                    <Button size="small" onClick={() => setDialogOpen(false)}>Cancelar</Button>
                    <Button size="small" variant="contained" onClick={handleSave}>Guardar</Button>
                </DialogActions>
            </Dialog>

            {/* Delete confirmation Dialog */}
            <Dialog open={confirmOpen} onClose={() => setConfirmOpen(false)}>
                <DialogTitle sx={{ fontSize: 15, fontWeight: 500 }}>Confirmar eliminación</DialogTitle>
                <DialogContent>
                    <Typography sx={{ fontSize: 13 }}>
                        ¿Estás seguro de que deseas eliminar este permiso?
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

export default Permissions;
