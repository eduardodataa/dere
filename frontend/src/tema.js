import { createTheme } from '@mui/material/styles';

const semanticaClara = {
  success: { main: '#2E7D32' },
  error: { main: '#D32F2F' },
  warning: { main: '#ED6C02' },
  info: { main: '#0288D1' },
};

const semanticaEscura = {
  success: { main: '#4ADE80' },
  error: { main: '#F87171' },
  warning: { main: '#FBBF24' },
  info: { main: '#60A5FA' },
};

export const temaDere = createTheme({
  cssVariables: {
    colorSchemeSelector: 'data-mui-color-scheme',
  },
  defaultColorScheme: 'light',
  typography: {
    fontFamily: 'Inter, system-ui, sans-serif',
  },
  shape: { borderRadius: 9 },
  colorSchemes: {
    light: {
      palette: {
        primary: { main: '#1976D2' },
        background: { default: '#F4F6FA', paper: '#FFFFFF' },
        text: { primary: '#111827', secondary: '#64748B' },
        divider: '#E2E8F0',
        ...semanticaClara,
      },
    },
    dark: {
      palette: {
        primary: { main: '#60A5FA' },
        background: { default: '#0F172A', paper: '#1E293B' },
        text: { primary: '#F8FAFC', secondary: '#CBD5E1' },
        divider: '#334155',
        ...semanticaEscura,
      },
    },
  },
  components: {
    MuiCard: { styleOverrides: { root: { borderRadius: 14 } } },
    MuiButton: { styleOverrides: { root: { borderRadius: 9 } } },
    MuiSnackbarContent: {
      styleOverrides: {
        root: ({ theme }) => theme.applyStyles('dark', {
          backgroundColor: '#263449',
          color: '#F8FAFC',
        }),
      },
    },
  },
});
