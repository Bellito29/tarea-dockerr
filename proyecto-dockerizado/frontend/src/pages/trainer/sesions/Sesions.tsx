import { useEffect, useState } from 'react';
import {
    Alert,
    Box,
    Button,
    CircularProgress,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    IconButton,
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
import { Add, Delete, Edit } from '@mui/icons-material';
import {
    createSesion,
    deleteSesion,
    getSesions,
    updateSesion,
} from './services/sesions.service';

interface SesionRow {
    id: number;
    sesionDate: string;
    duration: number;
}

type SesionForm = {
    sesionDate: string;
    duration: string;
};

const EMPTY_FORM: SesionForm = { sesionDate: '', duration: '' };

function formatDate(isoString: string): string {
    if (!isoString) return '';
    return isoString.replace('T', ' ').slice(0, 16);
}

function Sesions() {
    const [rows, setRows] = useState<SesionRow[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [form, setForm] = useState<SesionForm>(EMPTY_FORM);
    const [editId, setEditId] = useState<number | null>(null);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [deleteId, setDeleteId] = useState<number | null>(null);

    const load = async () => {
        try {
            setLoading(true);
            setError(null);
            const data = await getSesions();
            setRows(data);
        } catch {
            setError('Error al cargar las sesiones');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { load(); }, []);

    const openCreate = () => {
        setForm(EMPTY_FORM);
        setEditId(null);
        setDialogOpen(true);
    };

    const openEdit = (row: SesionRow) => {
        const dateValue = row.sesionDate ? row.sesionDate.slice(0, 16) : '';
        setForm({ sesionDate: dateValue, duration: String(row.duration) });
        setEditId(row.id);
        setDialogOpen(true);
    };

    const openDelete = (id: number) => {
        setDeleteId(id);
        setConfirmOpen(true);
    };

    const handleSave = async () => {
        try {
            const payload = { sesionDate: form.sesionDate, duration: Number(form.duration) };
            if (editId) {
                await updateSesion(editId, payload);
            } else {
                await createSesion(payload);
            }
            setDialogOpen(false);
            load();
        } catch {
            setError('Error al guardar la sesión');
        }
    };

    const handleDelete = async () => {
        try {
            if (deleteId !== null) await deleteSesion(deleteId);
            setConfirmOpen(false);
            load();
        } catch {
            setError('Error al eliminar la sesión');
        }
    };

    return (
        <Box>
            <Stack direction="row" sx={{ justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                <Typography variant="h5" sx={{ fontWeight: 700 }}>Sesiones</Typography>
                <Button variant="contained" startIcon={<Add />} onClick={openCreate}>
                    Nueva Sesión
                </Button>
            </Stack>

            {error && (
                <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError(null)}>
                    {error}
                </Alert>
            )}

            {loading ? (
                <Box sx={{ display: 'flex', justifyContent: 'center', py: 6 }}>
                    <CircularProgress />
                </Box>
            ) : (
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell>Fecha</TableCell>
                                <TableCell>Duración (min)</TableCell>
                                <TableCell align="right">Acciones</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row) => (
                                <TableRow key={row.id} hover>
                                    <TableCell>{row.id}</TableCell>
                                    <TableCell>{formatDate(row.sesionDate)}</TableCell>
                                    <TableCell>{row.duration}</TableCell>
                                    <TableCell align="right">
                                        <IconButton size="small" onClick={() => openEdit(row)}>
                                            <Edit fontSize="small" />
                                        </IconButton>
                                        <IconButton size="small" color="error" onClick={() => openDelete(row.id)}>
                                            <Delete fontSize="small" />
                                        </IconButton>
                                    </TableCell>
                                </TableRow>
                            ))}
                            {rows.length === 0 && (
                                <TableRow>
                                    <TableCell colSpan={4} align="center" sx={{ py: 4 }}>
                                        No hay sesiones registradas
                                    </TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}

            <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
                <DialogTitle>{editId ? 'Editar Sesión' : 'Nueva Sesión'}</DialogTitle>
                <DialogContent>
                    <Stack spacing={2} sx={{ mt: 1 }}>
                        <TextField
                            label="Fecha y hora"
                            type="datetime-local"
                            value={form.sesionDate}
                            onChange={(e) => setForm({ ...form, sesionDate: e.target.value })}
                            fullWidth
                            slotProps={{ inputLabel: { shrink: true } }}
                        />
                        <TextField
                            label="Duración (minutos)"
                            type="number"
                            value={form.duration}
                            onChange={(e) => setForm({ ...form, duration: e.target.value })}
                            fullWidth
                            slotProps={{ htmlInput: { min: 1 } }}
                        />
                    </Stack>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setDialogOpen(false)}>Cancelar</Button>
                    <Button variant="contained" onClick={handleSave}>Guardar</Button>
                </DialogActions>
            </Dialog>

            <Dialog open={confirmOpen} onClose={() => setConfirmOpen(false)}>
                <DialogTitle>Confirmar eliminación</DialogTitle>
                <DialogContent>
                    <Typography>¿Estás seguro de que deseas eliminar esta sesión?</Typography>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setConfirmOpen(false)}>Cancelar</Button>
                    <Button variant="contained" color="error" onClick={handleDelete}>
                        Eliminar
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
}

export default Sesions;
