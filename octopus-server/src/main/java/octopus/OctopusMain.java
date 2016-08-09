package octopus;

import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;

import octopus.server.components.ftpserver.OctopusFTPServer;
import octopus.server.components.restServer.OctopusRestServer;

public class OctopusMain {

    static OctopusMain main;
    String octopusHome;

    OServer server;
    OctopusFTPServer ftpServer;

    public static void main(String[] args) throws java.lang.Exception
    {
        main = new OctopusMain();
        main.startOrientdb();
        main.startFTPServer();
        main.startRestServer();
    }

	public OctopusMain()
    {
		initializeOctopusHome();
		tryToLoadConfigFile();
    }

    private void tryToLoadConfigFile()
    {
    	String configFilename = octopusHome + "/conf/octopus.conf";
    	OctopusConfigFile configFile = new OctopusConfigFile(configFilename);
    	configFile.transferToEnvironment();
    }

	private void initializeOctopusHome()
	{
		octopusHome = System.getProperty("OCTOPUS_HOME");

        if (octopusHome == null)
        {
            throw new RuntimeException("System property OCTOPUS_HOME not defined.");
        }
	}

	public void startOrientdb() throws java.lang.Exception
    {
        System.setProperty("ORIENTDB_HOME",octopusHome + "/orientdb");
        System.setProperty("orientdb.www.path",octopusHome +"/orientdb/www");
        System.setProperty("orientdb.config.file", octopusHome + "/conf/orientdb-server-config.xml");

        server = OServerMain.create();
        server.startup();
        server.activate();
    }

    private void startFTPServer()
    {
		ftpServer = new OctopusFTPServer();
		ftpServer.start(octopusHome);
    }

    private void startRestServer()
    {
		OctopusRestServer.start();
	}

}
