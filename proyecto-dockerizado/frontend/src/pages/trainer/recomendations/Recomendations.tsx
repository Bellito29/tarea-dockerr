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
    createRecomendation,
    deleteRecomendation,
    getRecomendations,
    updateRecomendation,
} from './services/recomendations.service';

interface RecomendationRow {
    id: number;
    description: string;
}

type RecomendationForm = {
    description: string;
};

const EMPTY_FORM: RecomendationForm = { description: '' };

function Recomendations() {
    const [rows, setRows] = useState<RecomendationRow[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [form, setForm] = useState<RecomendationForm>(EMPTY_FORM);
    const [editId, setEditId] = useState<number | null>(null);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [deleteId, setDeleteId] = useState<number | null>(null);

    const load = async () => {
        try {
            setLoading(true);
            setError(null);
            const data = await getRecomendations();
            setRows(data);
        } catch {
            setError('Error al cargar las recomendaciones');
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

    const openEdit = (row: RecomendationRow) => {
        setForm({ description: row.description });
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
                await updateRecomendation(editId, form);
            } else {
                await createRecomendation(form);
            }
            setDialogOpen(false);
            load();
        } catch {
            setError('Error al guardar la recomendación');
        }
    };

    const handleDelete = async () => {
        try {
            if (deleteId !== null) await deleteRecomendation(deleteId);
            setConfirmOpen(false);
            load();
        } catch {
            setError('Error al eliminar la recomendación');
        }
    };

    return (
        <Box>
            <Stack direction="row" sx={{ justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                <Typography variant="h5" sx={{ fontWeight: 700 }}>Recomendaciones</Typography>
                <Button variant="contained" startIcon={<Add />} onClick={openCreate}>
                    Nueva Recomendación
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
                                <TableCell>Descripción</TableCell>
                                <TableCell align="right">Acciones</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row) => (
                                <TableRow key={row.id} hover>
                                    <TableCell>{row.id}</TableCell>
                                    <TableCell>{row.description}</TableCell>
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
                                    <TableCell colSpan={3} align="center" sx={{ py: 4 }}>
                                        No hay recomendaciones registradas
                                    </TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}

            <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
                <DialogTitle>{editId ? 'Editar Recomendación' : 'Nueva Recomendación'}</DialogTitle>
                <DialogContent>
                    <Stack spacing={2} sx={{ mt: 1 }}>
                        <TextField
                            label="Descripción"
                            value={form.description}
                            onChange={(e) => setForm({ description: e.target.value })}
                            fullWidth
                            multiline
                            rows={3}
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
                    <Typography>¿Estás seguro de que deseas eliminar esta recomendación?</Typography>
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

export default Recomendations;
