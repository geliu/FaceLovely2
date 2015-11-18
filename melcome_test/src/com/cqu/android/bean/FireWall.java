package com.cqu.android.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.cqu.android.Activity.R;

public class FireWall {
	
    /** special application UID used to indicate "any application" */
    public static final int SPECIAL_UID_ANY = -10;
    /** special application UID used to indicate the Linux Kernel */
    public static final int SPECIAL_UID_KERNEL      = -11;

    /** root script filename */
    private static final String SCRIPT_FILE = "droidwall.sh";

    private static List<Integer> blackList=new LinkedList<Integer>();
    public static void initBlackList(){
    	
    }
    
    /**
     * Purge and re-add all rules (internal implementation).
     * @param ctx application context (mandatory)
     * @param uidsWifi list of selected UIDs for WIFI to allow or disallow (depending on the working mode)
     * @param uids3g list of selected UIDs for 2G/3G to allow or disallow (depending on the working mode)
     * @param showErrors indicates if errors should be alerted
     */
        public static String applyIptablesRulesImpl(Context ctx, List<Integer> uids3g,boolean showErrors) {
                if (ctx == null) {
                        return "";
                }
                assertBinaries(ctx, showErrors);
                
                final String ITFS_3G[] = {"rmnet+","pdp+","ppp+","uwbr+","wimax+","vsnet+"};
                //final String ITFS_3G[] = {"rmnet+","pdp+","ppp+","uwbr+"};
                final boolean whitelist = false;
                final boolean blacklist = !whitelist;

        final StringBuilder script = new StringBuilder();
                try {
                        script.append(scriptHeader(ctx));
                        script.append("" +
                                "$IPTABLES --version || exit 1\n" +
                                "# Create the droidwall chains if necessary\n" +
                                "$IPTABLES -L droidwall >/dev/null 2>/dev/null || $IPTABLES --new droidwall || exit 2\n" +
                                "$IPTABLES -L droidwall-3g >/dev/null 2>/dev/null || $IPTABLES --new droidwall-3g || exit 3\n" +
                                "$IPTABLES -L droidwall-wifi >/dev/null 2>/dev/null || $IPTABLES --new droidwall-wifi || exit 4\n" +
                                "$IPTABLES -L droidwall-reject >/dev/null 2>/dev/null || $IPTABLES --new droidwall-reject || exit 5\n" +
                                "# Add droidwall chain to OUTPUT chain if necessary\n" +
                                "$IPTABLES -L OUTPUT | $GREP -q droidwall || $IPTABLES -A OUTPUT -j droidwall || exit 6\n" +
                                "# Flush existing rules\n" +
                                "$IPTABLES -F droidwall || exit 7\n" +
                                "$IPTABLES -F droidwall-3g || exit 8\n" +
                                "$IPTABLES -F droidwall-wifi || exit 9\n" +
                                "$IPTABLES -F droidwall-reject || exit 10\n" +
                        "");
                        // Check if logging is enabled

                                script.append("" +
                                        "# Create the reject rule (log disabled)\n" +
                                        "$IPTABLES -A droidwall-reject -j REJECT || exit 11\n" +
                                "");
                        script.append("# Main rules (per interface)\n");
                        for (final String itf : ITFS_3G) {
                                script.append("$IPTABLES -A droidwall -o ").append(itf).append(" -j droidwall-3g || exit\n");
                        }

                        
                        script.append("# Filtering rules\n");
                        final String targetRule = (whitelist ? "RETURN" : "droidwall-reject");
                        final boolean any_3g = uids3g.indexOf(SPECIAL_UID_ANY) >= 0;

                        if (any_3g) {
                                if (blacklist) {
                                        /* block any application on this interface */
                                        script.append("$IPTABLES -A droidwall-3g -j ").append(targetRule).append(" || exit\n");
                                }
                        } else {
                                /* release/block individual applications on this interface */
                                for (final Integer uid : uids3g) {
                                        if (uid >= 0) script.append("$IPTABLES -A droidwall-3g -m owner --uid-owner ").append(uid).append(" -j ").append(targetRule).append(" || exit\n");
                                }
                        }

                        if (whitelist) {
                                if (!any_3g) {
                                        if (uids3g.indexOf(SPECIAL_UID_KERNEL) >= 0) {
                                                script.append("# hack to allow kernel packets on white-list\n");
                                                script.append("$IPTABLES -A droidwall-3g -m owner --uid-owner 0:999999999 -j droidwall-reject || exit\n");
                                        } else {
                                                script.append("$IPTABLES -A droidwall-3g -j droidwall-reject || exit\n");
                                        }
                                }

                        } else {
                                if (uids3g.indexOf(SPECIAL_UID_KERNEL) >= 0) {
                                        script.append("# hack to BLOCK kernel packets on black-list\n");
                                        script.append("$IPTABLES -A droidwall-3g -m owner --uid-owner 0:999999999 -j RETURN || exit\n");
                                        script.append("$IPTABLES -A droidwall-3g -j droidwall-reject || exit\n");
                                }

                        }
                final StringBuilder res = new StringBuilder();
                Log.v("lck",script.toString());
                int code=runScript(ctx, script.toString(), res,40000,true);
                if(showErrors&&code<0){
                	alert(ctx, ctx.getResources().getString(R.string.iptables_applyRule_error) + "\n");
                }
                return res.toString();

                } catch (Exception e) {
                }
                return "error";
    }
	
	/**
     * Display a simple alert box
     * @param ctx context
     * @param msg message
     */
    public static void alert(Context ctx, CharSequence msg) {
        if (ctx != null) {
                new AlertDialog.Builder(ctx)
                .setNeutralButton(android.R.string.ok, null)
                .setMessage(msg)
                .show();
        }
     }
    
	/**
	 * Check if we have root access
	 * @param ctx mandatory context
     * @param showErrors indicates if errors should be alerted
	 * @return boolean true if we have root
	 */
	public static boolean hasRootAccess(Context ctx, boolean showErrors) {
		final StringBuilder res = new StringBuilder();
		try {
			// Run an empty script just to check root access
			if (runScript(ctx, "exit 0", res,-1,true) == 0) {
				return true;
			}
		} catch (Exception e) {
		}
		if (showErrors) {
			alert(ctx, ctx.getResources().getString(R.string.iptables_hasNotRoot_error) + res.toString());
		}
		return false;
	}
	
	
    /**
	 * 清除所有规则
	 * @return	执行结果信息
	 */
	public static boolean purgeIptables(Context ctx, boolean showErrors) {
    	StringBuilder res = new StringBuilder();
		try {
			assertBinaries(ctx, showErrors);
			int code = runScript(ctx, scriptHeader(ctx) +
					"$IPTABLES -F droidwall\n" +
					"$IPTABLES -F droidwall-reject\n" +
					"$IPTABLES -F droidwall-3g\n" +
					"$IPTABLES -F droidwall-wifi\n", res,1,true);
			if (showErrors&&code == -1) {
				if (showErrors) alert(ctx, ctx.getResources().getString(R.string.iptables_purge_error) + "\n" + res);
				return false;
			}
			return true;
		} catch (Exception e) {
			if (showErrors) alert(ctx, ctx.getResources().getString(R.string.iptables_purge_error)+"\n"+e);
			return false;
		}
    }
	
	 
    
    /**
	 * Asserts that the binary files are installed in the cache directory.
	 * @param ctx context
     * @param showErrors indicates if errors should be alerted
	 * @return false if the binary files could not be installed
	 */
	public static boolean assertBinaries(Context ctx, boolean showErrors) {
		boolean changed = false;
		try {
			// Check iptables_g1
			File file = new File(ctx.getCacheDir(), "iptables_g1");
			if (!file.exists()) {
				copyRawFile(ctx, R.raw.iptables_g1, file, "755");
				changed = true;
			}
			// Check iptables_n1
			file = new File(ctx.getCacheDir(), "iptables_n1");
			if (!file.exists()) {
				copyRawFile(ctx, R.raw.iptables_n1, file, "755");
				changed = true;
			}
			// Check busybox
			file = new File(ctx.getCacheDir(), "busybox_g1");
			if (!file.exists()) {
				copyRawFile(ctx, R.raw.busybox_g1, file, "755");
				changed = true;
			}
			if (changed) {
				//Toast.makeText(ctx, R.string.toast_bin_installed, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			if (showErrors) alert(ctx, ctx.getResources().getString(R.string.iptables_binaryFiles_error)+"\n"+e);
			return false;
		}
		return true;
	}
	
	/**
	 * Copies a raw resource file, given its ID to the given location
	 * @param ctx context
	 * @param resid resource id
	 * @param file destination file
	 * @param mode file permissions (E.g.: "755")
	 * @throws IOException on error
	 * @throws InterruptedException when interrupted
	 */
	private static void copyRawFile(Context ctx, int resid, File file, String mode) throws IOException, InterruptedException
	{
		final String abspath = file.getAbsolutePath();
		// Write the iptables binary
		final FileOutputStream out = new FileOutputStream(file);
		final InputStream is = ctx.getResources().openRawResource(resid);
		byte buf[] = new byte[1024];
		int len;
		while ((len = is.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.close();
		is.close();
		// Change the permissions
		Runtime.getRuntime().exec("chmod "+mode+" "+abspath).waitFor();
	}
	
	
	/**
     * Create the generic shell script header used to determine which iptables binary to use.
     * @param ctx context
     * @return script header
     */
    private static String scriptHeader(Context ctx) {
    	final String dir = ctx.getCacheDir().getAbsolutePath();
		return "" +
			"IPTABLES=iptables\n" +
			"BUSYBOX=busybox\n" +
			"GREP=grep\n" +
			"ECHO=echo\n" +
			"# Try to find busybox\n" +
			"if " + dir + "/busybox_g1 --help >/dev/null 2>/dev/null ; then\n" +
			"	BUSYBOX="+dir+"/busybox_g1\n" +
			"	GREP=\"$BUSYBOX grep\"\n" +
			"	ECHO=\"$BUSYBOX echo\"\n" +
			"elif busybox --help >/dev/null 2>/dev/null ; then\n" +
			"	BUSYBOX=busybox\n" +
			"elif /system/xbin/busybox --help >/dev/null 2>/dev/null ; then\n" +
			"	BUSYBOX=/system/xbin/busybox\n" +
			"elif /system/bin/busybox --help >/dev/null 2>/dev/null ; then\n" +
			"	BUSYBOX=/system/bin/busybox\n" +
			"fi\n" +
			"# Try to find grep\n" +
			"if ! $ECHO 1 | $GREP -q 1 >/dev/null 2>/dev/null ; then\n" +
			"	if $ECHO 1 | $BUSYBOX grep -q 1 >/dev/null 2>/dev/null ; then\n" +
			"		GREP=\"$BUSYBOX grep\"\n" +
			"	fi\n" +
			"	# Grep is absolutely required\n" +
			"	if ! $ECHO 1 | $GREP -q 1 >/dev/null 2>/dev/null ; then\n" +
			"		$ECHO The grep command is required. DroidWall will not work.\n" +
			"		exit 1\n" +
			"	fi\n" +
			"fi\n" +
			"# Try to find iptables\n" +
			"if " + dir + "/iptables_g1 --version >/dev/null 2>/dev/null ; then\n" +
			"	IPTABLES="+dir+"/iptables_g1\n" +
			"elif " + dir + "/iptables_n1 --version >/dev/null 2>/dev/null ; then\n" +
			"	IPTABLES="+dir+"/iptables_n1\n" +
			"fi\n" +
			"";
    }
    
    /**
     * Runs a script, wither as root or as a regular user (multiple commands separated by "\n").
         * @param ctx mandatory context
     * @param script the script to be executed
     * @param res the script output response (stdout + stderr)
     * @param timeout timeout in milliseconds (-1 for none)
     * @return the script exit code
     */
        public static int runScript(Context ctx, String script, StringBuilder res, long timeout, boolean asroot) {
                final File file = new File(ctx.getDir("bin",0), SCRIPT_FILE);
                final ScriptRunner runner = new ScriptRunner(file, script, res, asroot);
                runner.start();
                try {
                        if (timeout > 0) {
                                runner.join(timeout);
                        } else {
                                runner.join();
                        }
                        if (runner.isAlive()) {
                                // Timed-out
                                runner.interrupt();
                                runner.join(150);
                                runner.destroy();
                                runner.join(50);
                        }
                } catch (InterruptedException ex) {}
                return runner.exitcode;
        }
        
        
        
        /**
         * Internal thread used to execute scripts (as root or not).
         */
        private static final class ScriptRunner extends Thread {
                private final File file;
                private final String script;
                private final StringBuilder res;
                private final boolean asroot;
                public int exitcode = -1;
                private Process exec;
                
                /**
                 * Creates a new script runner.
                 * @param file temporary script file
                 * @param script script to run
                 * @param res response output
                 * @param asroot if true, executes the script as root
                 */
                public ScriptRunner(File file, String script, StringBuilder res, boolean asroot) {
                        this.file = file;
                        this.script = script;
                        this.res = res;
                        this.asroot = asroot;
                }
                @Override
                public void run() {
                        try {
                                file.createNewFile();
                                final String abspath = file.getAbsolutePath();
                                // make sure we have execution permission on the script file
                                Runtime.getRuntime().exec("chmod 777 "+abspath).waitFor();
                                // Write the script to be executed
                                final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file));
                                if (new File("/system/bin/sh").exists()) {
                                        out.write("#!/system/bin/sh\n");
                                }
                                out.write(script);
                                if (!script.endsWith("\n")) out.write("\n");
                                out.write("exit\n");
                                out.flush();
                                out.close();
                                if (this.asroot) {
                                        // Create the "su" request to run the script
                                        exec = Runtime.getRuntime().exec("su -c "+abspath);
                                } else {
                                        // Create the "sh" request to run the script
                                        exec = Runtime.getRuntime().exec("sh "+abspath);
                                }
                                InputStreamReader r = new InputStreamReader(exec.getInputStream());
                                final char buf[] = new char[1024];
                                int read = 0;
                                // Consume the "stdout"
                                while ((read=r.read(buf)) != -1) {
                                        if (res != null) res.append(buf, 0, read);
                                }
                                // Consume the "stderr"
                                r = new InputStreamReader(exec.getErrorStream());
                                read=0;
                                while ((read=r.read(buf)) != -1) {
                                        if (res != null) res.append(buf, 0, read);
                                }
                                // get the process exit code
                                if (exec != null) this.exitcode = exec.waitFor();
                        } catch (InterruptedException ex) {
                                if (res != null) res.append("\nOperation timed-out");
                        } catch (Exception ex) {
                                if (res != null) res.append("\n" + ex);
                        } finally {
                                destroy();
                        }
                }
                /**
                 * Destroy this script runner
                 */
                public synchronized void destroy() {
                        if (exec != null) exec.destroy();
                        exec = null;
                }
        }
}
