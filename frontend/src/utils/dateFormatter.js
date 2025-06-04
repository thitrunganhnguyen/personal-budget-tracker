export const formatMonth = (monthNumber) => {
  const date = new Date(2000, monthNumber - 1);
  return date.toLocaleString('default', { month: 'long' });
};
