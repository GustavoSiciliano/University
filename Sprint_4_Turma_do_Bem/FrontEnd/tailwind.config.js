/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        tdb: {
          green:  '#7ab800',
          teal:   '#2d4a1e',
          yellow: '#f5821f',
          orange: '#f5821f',
          dark:   '#2d4a1e',
          light:  '#f4f9ec',
          soft:   '#fff8f2',
        }
      },
      fontFamily: {
        display: ['Nunito', 'sans-serif'],
        body:    ['Lato', 'sans-serif'],
      },
      keyframes: {
        'fade-in-up': { '0%': { opacity: '0', transform: 'translateY(16px)' }, '100%': { opacity: '1', transform: 'translateY(0)' } },
        'fade-in':    { '0%': { opacity: '0' }, '100%': { opacity: '1' } },
        'pulse-slow': { '0%,100%': { opacity: '1' }, '50%': { opacity: '.4' } },
      },
      animation: {
        'fade-in-up':  'fade-in-up 0.4s ease both',
        'fade-in':     'fade-in 0.3s ease both',
        'pulse-slow':  'pulse-slow 2s ease-in-out infinite',
      },
    },
  },
  plugins: [],
}
