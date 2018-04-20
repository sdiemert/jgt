package com.sdiemert.jgt.fs;

import com.sdiemert.jgt.core.GTSystem;
import com.sdiemert.jgt.core.Graph;
import com.sdiemert.jgt.core.GraphException;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Builds up a GTSystem and/or Graphs from the contents of a file.
 */
public interface FileParser {

    Graph loadGraph(String filePath) throws IOException, JSONException, GraphException;

    GTSystem loadSystem(String filePath) throws IOException, JSONException, GraphException;

}
