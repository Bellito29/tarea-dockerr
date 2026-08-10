import { Box, Typography } from '@mui/material';

interface SessionItemProps {
    initials: string;
    name: string;
    date: string;
    routineName: string;
    duration?: string | null;
    volume?: string | null;
    exercises: string[];
    extraCount: number;
}

function SessionItem({ initials, name, date, routineName, duration, volume, exercises, extraCount }: SessionItemProps) {
    return (
        <Box sx={{ display: 'flex', gap: '10px', p: '14px 18px', borderBottom: '1px solid', borderColor: 'divider', '&:last-child': { borderBottom: 'none' } }}>
            <Box sx={{
                width: 34, height: 34, borderRadius: '50%',
                background: '#1a1a2e', display: 'flex',
                alignItems: 'center', justifyContent: 'center',
                fontSize: 11, fontWeight: 500, color: '#fff', flexShrink: 0,
            }}>
                {initials}
            </Box>
            <Box sx={{ flex: 1, minWidth: 0 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: '6px', mb: 0.25 }}>
                    <Typography sx={{ fontSize: 12, fontWeight: 500, color: 'text.primary' }}>{name}</Typography>
                    <Typography sx={{ fontSize: 10, color: 'text.disabled' }}>{date}</Typography>
                </Box>
                <Typography sx={{ fontSize: 13, fontWeight: 500, color: 'text.primary', mb: 0.5 }}>{routineName}</Typography>
                <Box sx={{ display: 'flex', gap: '14px', mb: 0.75 }}>
                    {duration && (
                        <Typography sx={{ fontSize: 11, color: 'text.secondary' }}>
                            Duración <Box component="span" sx={{ fontWeight: 500, color: 'text.primary' }}>{duration}</Box>
                        </Typography>
                    )}
                    {volume && (
                        <Typography sx={{ fontSize: 11, color: 'text.secondary' }}>
                            Volumen <Box component="span" sx={{ fontWeight: 500, color: 'text.primary' }}>{volume}</Box>
                        </Typography>
                    )}
                </Box>
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: '3px' }}>
                    {exercises.map((ex, i) => (
                        <Box key={i} sx={{ display: 'flex', alignItems: 'center', gap: '7px' }}>
                            <Box sx={{ width: 5, height: 5, borderRadius: '50%', background: '#4f46e5', flexShrink: 0 }} />
                            <Typography sx={{ fontSize: 12, color: 'text.secondary' }}>{ex}</Typography>
                        </Box>
                    ))}
                </Box>
                {extraCount > 0 && (
                    <Typography sx={{ fontSize: 11, color: 'text.disabled', mt: 0.5, cursor: 'pointer' }}>
                        Ver {extraCount} ejercicios más
                    </Typography>
                )}
            </Box>
        </Box>
    );
}

export default SessionItem;
