import {
    Box,
    Button,
    Container,
    Paper,
    Stack,
    TextField,
    Typography,
} from '@mui/material';
import { useContext, useRef } from 'react';
import { login } from './services/login.service';
import { useNavigate } from 'react-router';
import AuthContext from '../../context/AuthContext';

function SignIn() {
    const ref = useRef<HTMLFormElement>(null);
    const nav = useNavigate();
    const auth = useContext(AuthContext);

    const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!ref.current) return;
        const formData = new FormData(ref.current);
        const data = Object.fromEntries(formData) as { username: string; password: string };

        try {
            const response = await login(data.username, data.password);
            const token = response.accessToken as string;
            auth?.login(token);

            const payload = JSON.parse(atob(token.split('.')[1])) as { role?: string };
            const role = payload.role ?? '';

            if (role === 'ROLE_ADMIN') {
                nav('/admin');
            } else if (role === 'ROLE_TRAINER') {
                nav('/trainer');
            } else if (role === 'ROLE_STUDENT') {
                nav('/studentDashboard');
            }
        } catch (error) {
            console.error('Login fallido:', error);
        }
    };

    return (
        <Box sx={{ minHeight: '100vh', display: 'grid', placeItems: 'center', px: 2 }}>
            <Container maxWidth="sm">
                <Paper elevation={3} sx={{ p: { xs: 3, md: 4 } }}>
                    <Stack spacing={3} component="form" ref={ref} onSubmit={onSubmit}>
                        <Box>
                            <Typography variant="h4" component="h1" sx={{ fontWeight: 700 }}>
                                Login
                            </Typography>
                        </Box>

                        <TextField
                            label="Username"
                            name="username"
                            fullWidth
                        />

                        <TextField
                            label="Password"
                            name="password"
                            type="password"
                            fullWidth
                        />

                        <Button type="submit" variant="contained">
                            Entrar
                        </Button>
                    </Stack>
                </Paper>
            </Container>
        </Box>
    );
}

export default SignIn;
