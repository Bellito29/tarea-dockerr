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
    createExercise,
    deleteExercise,
    getExercises,
    updateExercise,
} from './services/exercises.service';

interface ExerciseRow {
    id: number;
    name: string;
    type: string;
    description: string;
    duration: number;
    difficulty: string;
    videoUrl?: string;
}

type ExerciseForm = {
    name: string;
    type: string;
    description: string;
    duration: string;
    difficulty: string;
    videoUrl: string;
};

const EMPTY_FORM: ExerciseForm = {
    name: '',
    type: 'Strength',
    description: '',
    duration: '',
    difficulty: 'Medium',
    videoUrl: '',
};

function Exercises() {
    const [rows, setRows] = useState<ExerciseRow[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [form, setForm] = useState<ExerciseForm>(EMPTY_FORM);
    const [editId, setEditId] = useState<number | null>(null);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [deleteId, setDeleteId] = useState<number | null>(null);

    const load = async () => {
        try {
            setLoading(true);
            setError(null);
            const data = await getExercises();
            setRows(data);
        } catch {
            setError('Error al cargar los ejercicios');
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

    const openEdit = (row: ExerciseRow) => {
        setForm({
            name: row.name,
            type: row.type,
            description: row.description,
            duration: String(row.duration),
            difficulty: row.difficulty,
            videoUrl: row.videoUrl ?? '',
        });
        setEditId(row.id);
        setDialogOpen(true);
    };

    const openDelete = (id: number) => {
        setDeleteId(id);
        setConfirmOpen(true);
    };

    const handleSave = async () => {
        try {
            const payload = { ...form, duration: Number(form.duration) };
            if (editId) {
                await updateExercise(editId, payload);
            } else {
                await createExercise(payload);
            }
            setDialogOpen(false);
            load();
        } catch {
            setError('Error al guardar el ejercicio');
        }
    };

    const handleDelete = async () => {
        try {
            if (deleteId !== null) await deleteExercise(deleteId);
            setConfirmOpen(false);
            load();
        } catch {
            setError('Error al eliminar el ejercicio');
        }
    };

    const field = (key: keyof ExerciseForm) => ({
        value: form[key],
        onChange: (e: React.ChangeEvent<HTMLInputElement>) => setForm({ ...form, [key]: e.target.value }),
        fullWidth: true,
    });

    return (
        <Box>
            <Stack direction="row" sx={{ justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                <Typography variant="h5" sx={{ fontWeight: 700 }}>Ejercicios</Typography>
                <Button variant="contained" startIcon={<Add />} onClick={openCreate}>
                    Nuevo Ejercicio
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
                                <TableCell>Tipo</TableCell>
                                <TableCell>Duración (min)</TableCell>
                                <TableCell>Dificultad</TableCell>
                                <TableCell>Descripción</TableCell>
                                <TableCell align="right">Acciones</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row) => (
                                <TableRow key={row.id} hover>
                                    <TableCell>{row.id}</TableCell>
                                    <TableCell>{row.name}</TableCell>
                                    <TableCell>{row.type}</TableCell>
                                    <TableCell>{row.duration}</TableCell>
                                    <TableCell>{row.difficulty}</TableCell>
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
                                    <TableCell colSpan={7} align="center" sx={{ py: 4 }}>
                                        No hay ejercicios registrados
                                    </TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}

            <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
                <DialogTitle>{editId ? 'Editar Ejercicio' : 'Nuevo Ejercicio'}</DialogTitle>
                <DialogContent>
                    <Stack spacing={2} sx={{ mt: 1 }}>
                        <TextField label="Nombre" {...field('name')} />
                        <FormControl fullWidth>
                            <InputLabel>Tipo</InputLabel>
                            <Select value={form.type} label="Tipo" onChange={(e) => setForm({ ...form, type: e.target.value })}>
                                <MenuItem value="Strength">Fuerza</MenuItem>
                                <MenuItem value="Core">Core</MenuItem>
                                <MenuItem value="Cardio">Cardio</MenuItem>
                                <MenuItem value="Endurance">Resistencia</MenuItem>
                            </Select>
                        </FormControl>
                        <TextField label="Descripción" {...field('description')} multiline rows={2} />
                        <TextField label="Duración (minutos)" {...field('duration')} type="number" slotProps={{ htmlInput: { min: 1 } }} />
                        <FormControl fullWidth>
                            <InputLabel>Dificultad</InputLabel>
                            <Select value={form.difficulty} label="Dificultad" onChange={(e) => setForm({ ...form, difficulty: e.target.value })}>
                                <MenuItem value="Easy">Fácil</MenuItem>
                                <MenuItem value="Medium">Media</MenuItem>
                                <MenuItem value="Hard">Difícil</MenuItem>
                            </Select>
                        </FormControl>
                        <TextField label="URL del Video" {...field('videoUrl')} />
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
                    <Typography>¿Estás seguro de que deseas eliminar este ejercicio?</Typography>
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

export default Exercises;
