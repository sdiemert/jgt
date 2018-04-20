package com.sdiemert.jgt.fs;

import com.sdiemert.jgt.core.GTSystem;
import com.sdiemert.jgt.core.Graph;
import com.sdiemert.jgt.core.GraphException;
import com.sdiemert.jgt.core.RuleException;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Builds up a GTSystem and/or Graphs from the contents of a file.
 */
public abstract class FileWriter {

    public abstract void writeGraph(Graph g, String filePath) throws IOException;

    public abstract void writeSystem(GTSystem s, String filePath) throws IOException;

    void toFile(String path, String content) throws IOException {
        File f = new File(path);
        FileUtils.writeStringToFile(f, content, "utf-8");
    }
}
