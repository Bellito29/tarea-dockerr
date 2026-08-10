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
    Snackbar,
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
import { Add, Delete, Edit, Wifi } from '@mui/icons-material';
import {
    createMessage,
    deleteMessage,
    getMessages,
    updateMessage,
} from './services/messages.service';
import { useAppSelector } from '../../../store/hooks';

interface MessageRow {
    id: number;
    content: string;
}

type MessageForm = {
    content: string;
};

const EMPTY_FORM: MessageForm = { content: '' };

function Messages() {
    const [rows, setRows] = useState<MessageRow[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [form, setForm] = useState<MessageForm>(EMPTY_FORM);
    const [editId, setEditId] = useState<number | null>(null);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [deleteId, setDeleteId] = useState<number | null>(null);
    const [snackOpen, setSnackOpen] = useState(false);
    const [snackText, setSnackText] = useState('');

    const { connected, realtimeMessages } = useAppSelector((s) => s.websocket);

    const load = async () => {
        try {
            setLoading(true);
            setError(null);
            const data = await getMessages();
            setRows(data);
        } catch {
            setError('Error al cargar los mensajes');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { load(); }, []);

    // Show snackbar and refresh list when a real-time message arrives
    useEffect(() => {
        if (realtimeMessages.length === 0) return;
        const latest = realtimeMessages[0];
        setSnackText(`Nuevo mensaje en tiempo real: "${latest.content}"`);
        setSnackOpen(true);
        setRows((prev) => {
            const exists = prev.some((r) => r.id === latest.id);
            if (exists) return prev;
            return [{ id: latest.id, content: latest.content }, ...prev];
        });
    }, [realtimeMessages]);

    const openCreate = () => {
        setForm(EMPTY_FORM);
        setEditId(null);
        setDialogOpen(true);
    };

    const openEdit = (row: MessageRow) => {
        setForm({ content: row.content });
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
                await updateMessage(editId, form);
            } else {
                await createMessage(form);
            }
            setDialogOpen(false);
            load();
        } catch {
            setError('Error al guardar el mensaje');
        }
    };

    const handleDelete = async () => {
        try {
            if (deleteId !== null) await deleteMessage(deleteId);
            setConfirmOpen(false);
            load();
        } catch {
            setError('Error al eliminar el mensaje');
        }
    };

    return (
        <Box>
            <Stack direction="row" sx={{ justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                <Stack direction="row" sx={{ alignItems: 'center' }} spacing={1}>
                    <Typography variant="h5" sx={{ fontWeight: 700 }}>Mensajes</Typography>
                    <Wifi
                        fontSize="small"
                        sx={{ color: connected ? 'success.main' : 'text.disabled' }}
                        titleAccess={connected ? 'WebSocket conectado' : 'WebSocket desconectado'}
                    />
                </Stack>
                <Button variant="contained" startIcon={<Add />} onClick={openCreate}>
                    Nuevo Mensaje
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
                                <TableCell>Contenido</TableCell>
                                <TableCell align="right">Acciones</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row) => (
                                <TableRow key={row.id} hover>
                                    <TableCell>{row.id}</TableCell>
                                    <TableCell>{row.content}</TableCell>
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
                                        No hay mensajes registrados
                                    </TableCell>
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}

            <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
                <DialogTitle>{editId ? 'Editar Mensaje' : 'Nuevo Mensaje'}</DialogTitle>
                <DialogContent>
                    <Stack spacing={2} sx={{ mt: 1 }}>
                        <TextField
                            label="Contenido"
                            value={form.content}
                            onChange={(e) => setForm({ content: e.target.value })}
                            fullWidth
                            multiline
                            rows={4}
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
                    <Typography>¿Estás seguro de que deseas eliminar este mensaje?</Typography>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setConfirmOpen(false)}>Cancelar</Button>
                    <Button variant="contained" color="error" onClick={handleDelete}>
                        Eliminar
                    </Button>
                </DialogActions>
            </Dialog>

            <Snackbar
                open={snackOpen}
                autoHideDuration={4000}
                onClose={() => setSnackOpen(false)}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
            >
                <Alert severity="info" onClose={() => setSnackOpen(false)}>
                    {snackText}
                </Alert>
            </Snackbar>
        </Box>
    );
}

export default Messages;
