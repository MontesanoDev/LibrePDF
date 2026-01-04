/*
 *
 *  * LibrePDF - A lightweight, native tool for manipulating PDF files.
 *  * Copyright (C) 2026 Leonardo Montemurro
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package it.leonardomontemurro.localpdf;

public enum Icons {
    MERGE("merge","Combine multiple PDF files into\na single document."),
    SPLIT("split","Extract specific pages or divide\na document into multiple\nindependent files."),
    ROTATE("rotate","Change the orientation of your\npages."),
    SWAP("swap","Reorder pages order."),
    METADATA("metadata","Clean or randomize metadata."),
    PDFTOJPEG("pdftojpeg","Convert your pdf to JPEG."),
    PROTECT("protect","Secure your documents with \nstrong encryption."),
    UNLOCK("unlock", "Remove password from pdf.");

    private final String name;
    private final String description;

    Icons(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription(){
        return description;
    }

}
