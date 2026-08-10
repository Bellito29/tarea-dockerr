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
    FormControl,
    IconButton,
    InputLabel,
    MenuItem,
    Paper,
    Select,
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
    createRoutine,
    deleteRoutine,
    getRoutines,
    updateRoutine,
} from './services/routines.service';

interface RoutineRow {
    id: number;
    routineName: string;
    description: string;
    visibility: string;
}

type RoutineForm = {
    routineName: string;
    description: string;
    visibility: string;
};

const EMPTY_FORM: RoutineForm = { routineName: '', description: '', visibility: 'PUBLIC' };

function Routines() {
    const [rows, setRows] = useState<RoutineRow[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [form, setForm] = useState<RoutineForm>(EMPTY_FORM);
    const [editId, setEditId] = useState<number | null>(null);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [deleteId, setDeleteId] = useState<number | null>(null);

    const load = async () => {
        try {
            setLoading(true);
            setError(null);
            const data = await getRoutines();
            setRows(data);
        } catch {
            setError('Error al cargar las rutinas');
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

    const openEdit = (row: RoutineRow) => {
        setForm({ routineName: row.routineName, description: row.description, visibility: row.visibility });
        setEditId(row.id);
        setDialogOpen(true);
    };

    const openDelete = (id: number) => {
        setDeleteId(id);
        setConfirmOpen(true);
    };

    const handleSave = async () => {
        try {
            if (editId) {
                await updateRoutine(editId, form);
            } else {
                await createRoutine(form);
            }
            setDialogOpen(false);
            load();
        } catch {
            setError('Error al guardar la rutina');
        }
    };

    const handleDelete = async () => {
        try {
            if (deleteId !== null) await deleteRoutine(deleteId);
            setConfirmOpen(false);
            load();
        } catch {
            setError('Error al eliminar la rutina');
        }
    };

    return (
        <Box>
            <Stack direction="row" sx={{ justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                <Typography variant="h5" sx={{ fontWeight: 700 }}>Rutinas</Typography>
                <Button variant="contained" startIcon={<Add />} onClick={openCreate}>
                    Nueva Rutina
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
                                <TableCell>Nombre</TableCell>
                                <TableCell>Descripción</TableCell>
                                <TableCell>Visibilidad</TableCell>
                                <TableCell align="right">Acciones</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row) => (
                                <TableRow key={row.id} hover>
                                    <TableCell>{row.id}</TableCell>
                                    <TableCell>{row.routineName}</TableCell>
                                    <TableCell>{row.description}</TableCell>
                                    <TableCell>{row.visibility}</TableCell>
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
                                    <TableCell colSpan={5} align="center" sx={{ py: 4 }}>
                                        No hay rutinas registradas
                                    </TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}

            <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
                <DialogTitle>{editId ? 'Editar Rutina' : 'Nueva Rutina'}</DialogTitle>
                <DialogContent>
                    <Stack spacing={2} sx={{ mt: 1 }}>
                        <TextField
                            label="Nombre"
                            value={form.routineName}
                            onChange={(e) => setForm({ ...form, routineName: e.target.value })}
                            fullWidth
                        />
                        <TextField
                            label="Descripción"
                            value={form.description}
                            onChange={(e) => setForm({ ...form, description: e.target.value })}
                            fullWidth
                            multiline
                            rows={3}
                        />
                        <FormControl fullWidth>
                            <InputLabel>Visibilidad</InputLabel>
                            <Select
                                value={form.visibility}
                                label="Visibilidad"
                                onChange={(e) => setForm({ ...form, visibility: e.target.value })}
                            >
                                <MenuItem value="PUBLIC">Pública</MenuItem>
                                <MenuItem value="PRIVATE">Privada</MenuItem>
                            </Select>
                        </FormControl>
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
                    <Typography>¿Estás seguro de que deseas eliminar esta rutina?</Typography>
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

export default Routines;
