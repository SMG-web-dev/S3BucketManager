tailwind.config = {
  theme: {
    extend: {
      colors: {
        'ash-gray': '#cad2c5',
        'cambridge-blue': '#84a98c',
        'hookers-green': '#52796f',
        'dark-slate-gray': '#354f52',
        'charcoal': '#2f3e46',
      },
      animation: {
        fadeIn: 'fadeIn 0.5s ease-in-out',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
      },
    },
  },
}