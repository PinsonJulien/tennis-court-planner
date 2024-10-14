export abstract class DateHelper {
    public static toDateWithoutTimezone(date: Date) : String {
        const tzoffset = date.getTimezoneOffset() * 60000;
        const withoutTimezone = new Date(date.valueOf() - tzoffset)
            .toISOString()
            .slice(0, -1);
        return withoutTimezone;
    }
}