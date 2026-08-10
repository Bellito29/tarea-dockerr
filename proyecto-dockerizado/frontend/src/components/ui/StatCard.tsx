import { ReactNode } from 'react';
import { Box, Typography } from '@mui/material';

interface StatCardProps {
    label: string;
    value: number | string;
    sub?: string;
    icon: ReactNode;
    iconColor?: string;
}

function StatCard({ label, value, sub, icon, iconColor }: StatCardProps) {
    return (
        <Box sx={{
            background: 'background.paper',
            border: '1px solid',
            borderColor: 'divider',
            borderRadius: 2,
            p: '14px 16px',
            flex: 1,
        }}>
            <Box sx={{ fontSize: 22, color: iconColor, mb: 0.5 }}>{icon}</Box>
            <Typography sx={{ fontSize: 11, color: 'text.secondary', mb: 0.25 }}>{label}</Typography>

            <Typography sx={{ fontSize: 24, fontWeight: 500, color: 'text.primary' }}>{value}</Typography>

            <Typography sx={{ fontSize: 10, color: 'text.disabled', mt: 0.25 }}>{sub}</Typography>
        </Box>
    );
}

export default StatCard;
