package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

/**
 * Command that constantly logs and updates data in the telemetry
 */
public class DataLog extends CommandBase {
    /**
     * An entry in the log
     */
    public static class Entry {
        private final String name;
        private final Supplier<String> supplier;

        /**
         * Constructs an entry
         * @param name the name of the entry
         * @param supplier the data supplier of the entry
         */
        public Entry(String name, Supplier<String> supplier) {
            this.name = name;
            this.supplier = supplier;
        }

        /**
         * Returns the name of the entry
         * @return the name of the entry
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the string of the data of the entry
         * @return the data of the entry as a string
         */
        public String getData() {
            return supplier.get();
        }
    }

    private final ArrayList<Entry> entries;

    /**
     * Create a data logger
     */
    public DataLog() {
        this.entries = new ArrayList<>();
    }

    @Override
    public void execute() {
        Constants.telemetry.clearAll();
        for (Entry entry : entries)
            Constants.telemetry.addData(entry.getName(), entry.getData());
        Constants.telemetry.update();
    }

    /**
     * Returns the entries in the logger
     * @return entries
     */
    public ArrayList<Entry> getEntries() { return entries; }

    /**
     * Appends the entry
     * @param entry entry to be added
     */
    public void addEntry(Entry entry) { entries.add(entry); }

    /**
     * Appends an entry of the name and supplier.
     * @param name the name of the entry
     * @param supplier the supplier of the string to the entry.
     */
    public void addEntry(String name, Supplier<String> supplier) {
        entries.add(new Entry(name, supplier));
    }

    /**
     * Appends an entry of the name and double supplier.
     * @param name the name of the entry
     * @param supplier the supplier of the double to the entry.
     */
    public void addEntry(String name, DoubleSupplier supplier) {
        entries.add(new Entry(name, () -> Double.toString(supplier.getAsDouble())));
    }

    /**
     * Removes an entry based on the name
     * @param name name of the entry to be removed
     */
    public void removeEntry(String name) {
        entries.removeIf((entry) -> entry.getName().equals(name));
    }
}