/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemasOperativos;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;


/**
 *
 * @author Dell
 */
public class MiRender extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocu, int row, int column) {

        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocu, row, column);
        
        cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 2, Color.GRAY));
        
        if (value instanceof String) {

            String valor = (String) value;

            if (column == 2) {

                if (valor.equals("R")) {

                    cell.setBackground(Color.black);
                    cell.setForeground(Color.black);

                } else if (valor.equals("A")) {

                    cell.setBackground(Color.blue);
                    cell.setForeground(Color.blue);
                } else if (valor.equals("E")) {
                    cell.setBackground(Color.red);
                    cell.setForeground(Color.red);
                } else if (valor.equals("B")) {
                    cell.setBackground(Color.GREEN);
                    cell.setForeground(Color.GREEN);
                } else {
                    cell.setBackground(Color.white);
                    cell.setForeground(Color.black);
                }

            } else if (column == 3) {

                if (valor.equals("R")) {

                    cell.setBackground(Color.black);
                    cell.setForeground(Color.black);

                } else if (valor.equals("A")) {

                    cell.setBackground(Color.blue);
                    cell.setForeground(Color.blue);
                } else if (valor.equals("E")) {
                    cell.setBackground(Color.red);
                    cell.setForeground(Color.red);
                } else if (valor.equals("B")) {
                    cell.setBackground(Color.GREEN);
                    cell.setForeground(Color.GREEN);
                } else {
                    cell.setBackground(Color.white);
                    cell.setForeground(Color.black);
                }

            } else if (column == 4) {

                if (valor.equals("R")) {

                    cell.setBackground(Color.black);
                    cell.setForeground(Color.black);

                } else if (valor.equals("A")) {

                    cell.setBackground(Color.blue);
                    cell.setForeground(Color.blue);
                } else if (valor.equals("E")) {
                    cell.setBackground(Color.red);
                    cell.setForeground(Color.red);
                } else if (valor.equals("B")) {
                    cell.setBackground(Color.GREEN);
                    cell.setForeground(Color.GREEN);
                } else {
                    cell.setBackground(Color.white);
                    cell.setForeground(Color.black);
                }

            } else if (column == 5) {

                if (valor.equals("R")) {

                    cell.setBackground(Color.black);
                    cell.setForeground(Color.black);

                } else if (valor.equals("A")) {

                    cell.setBackground(Color.blue);
                    cell.setForeground(Color.blue);
                } else if (valor.equals("E")) {
                    cell.setBackground(Color.red);
                    cell.setForeground(Color.red);
                } else if (valor.equals("B")) {
                    cell.setBackground(Color.GREEN);
                    cell.setForeground(Color.GREEN);
                } else {
                    cell.setBackground(Color.white);
                    cell.setForeground(Color.black);
                }

            } else if (column == 6) {

                if (valor.equals("R")) {

                    cell.setBackground(Color.black);
                    cell.setForeground(Color.black);

                } else if (valor.equals("A")) {

                    cell.setBackground(Color.blue);
                    cell.setForeground(Color.blue);
                } else if (valor.equals("E")) {
                    cell.setBackground(Color.red);
                    cell.setForeground(Color.red);
                } else if (valor.equals("B")) {
                    cell.setBackground(Color.GREEN);
                    cell.setForeground(Color.GREEN);
                } else {
                    cell.setBackground(Color.white);
                    cell.setForeground(Color.black);
                }

            } else {
                cell.setBackground(Color.white);
                cell.setForeground(Color.black);
            }

        }

        return cell;

    }

}
