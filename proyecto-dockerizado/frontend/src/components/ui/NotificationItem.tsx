import { ReactNode } from 'react';
import { Box, Typography } from '@mui/material';

interface NotificationItemProps {
    icon?: ReactNode;
    text: string;
    time: string;
}

function NotificationItem({ icon, text, time }: NotificationItemProps) {
    return (
        <Box sx={{ display: 'flex', alignItems: 'flex-start', gap: '8px', py: '7px', borderBottom: '1px solid', borderColor: 'divider', '&:last-child': { borderBottom: 'none' } }}>
            <Box sx={{
                width: 28, height: 28, borderRadius: '50%',
                background: 'action.hover',
                display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0,
            }}>
                {icon}
            </Box>
            <Box>
                <Typography sx={{ fontSize: 11, color: 'text.secondary', lineHeight: 1.4 }}
                    dangerouslySetInnerHTML={{ __html: text }} />
                <Typography sx={{ fontSize: 10, color: 'text.disabled', mt: 0.25 }}>{time}</Typography>
            </Box>
        </Box>
    );
}

export default NotificationItem;
