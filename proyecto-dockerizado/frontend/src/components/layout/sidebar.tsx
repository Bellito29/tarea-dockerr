import DashboardIcon from '@mui/icons-material/Dashboard';
import PeopleIcon from '@mui/icons-material/People';
import LockIcon from '@mui/icons-material/Lock';
import AdminPanelSettingsIcon from  '@mui/icons-material/AdminPanelSettings';

import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import { useNavigate } from 'react-router';
import MenuIcon from '@mui/icons-material/Menu';
import IconButton from '@mui/material/IconButton';

import { useState } from 'react';

export default function Sidebar() {
    const nav = useNavigate();

    const [open, setOpen] = useState(false);

    const toggleDrawer = (newOpen: boolean) => () => {
        setOpen(newOpen);
    };

    const menuItems = {
        ADMIN: [
            { label: 'Dashboard', path: '/admin/', icon: <DashboardIcon /> },
            { label: 'Usuarios',  path: '/admin/users',     icon: <PeopleIcon /> },
            { label: 'Roles',     path: '/admin/roles',     icon: <AdminPanelSettingsIcon /> },
            { label: 'Permisos',  path: '/admin/permissions', icon: <LockIcon /> },
        ]
    };

    const DrawerList = (
        <Box sx={{ width: 250, bgcolor: 'background.paper' }} role="presentation" onClick={toggleDrawer(false)}>
            <List>
                {[menuItems.ADMIN].flat().map((item) => (
                    <ListItem key={item.label} disablePadding>
                        <ListItemButton onClick={() => {
                            nav(item.path);
                            setOpen(false);
                        }}>
                            <ListItemIcon>
                                {item.icon}
                            </ListItemIcon>
                            <ListItemText primary={item.label} />
                        </ListItemButton>
                    </ListItem>
                ))}
            </List>
        </Box>
    );

    return (
        <div>
            <Box sx={{ bgcolor: 'background.paper' }}>
                <IconButton onClick={toggleDrawer(true)}>
                    <MenuIcon />
                </IconButton>
                <Drawer
                    open={open}
                    onClose={toggleDrawer(false)}
                    slotProps={{ paper: { sx: { bgcolor: 'background.paper' } } }}
                >
                    {DrawerList}
                </Drawer>
            </Box>
        </div>
    );
}
