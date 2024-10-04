export function getDate(date: Date | string): string {
    const optionsWithTime: Intl.DateTimeFormatOptions = {
        weekday: 'long', 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric', 
        hour: 'numeric', 
        minute: 'numeric'
    };

    const validDate = typeof date === 'string' ? new Date(date) : date;
    return validDate.toLocaleDateString('en-US', optionsWithTime);
}
