package com.stericson.RootTools;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class RootTools {

    /*
     *This class is the gateway to every functionality within the RootTools library.
     *The developer should only have access to this class and this class only.
     *This means that this class should be the only one to be public.
     *The rest of the classes within this library must not have the public modifier.
     *
     *All methods and Variables that the developer may need to have access to should be here.
     *
     *If a method, or a specific functionality, requires a fair amount of code, or work to be done,
     *then that functionality should probably be moved to its own class and the call to it done here.
     *For examples of this being done, look at the remount functionality.
     */

    //--------------------
    //# Public Variables #
    //--------------------

    public static boolean debugMode = false;

    //---------------------------
    //# Public Variable Getters #
    //---------------------------

    /**
     * This will return the environment variable $PATH
     *
     * @return <code>Set<String></code> A Set of Strings representing the environment variable $PATH
     * @throws Exception if we cannot return the $PATH variable
     */
    public static Set<String> getPath() throws Exception {
        if (InternalVariables.path != null) {
            return InternalVariables.path;
        } else {
            if (InternalMethods.instance().returnPath()) {
                return InternalVariables.path;
            } else {
                throw new Exception();
            }
        }
    }

    /**
     * This will return an ArrayList of the class Mount.
     * The class mount contains the following property's:
     * device
     * mountPoint
     * type
     * flags
     * <p/>
     * These will provide you with any information you need to work with the mount points.
     *
     * @return <code>ArrayList<Mount></code> an ArrayList of the class Mount.
     * @throws Exception if we cannot return the mount points.
     */
    public static ArrayList<Mount> getMounts() throws Exception {
        InternalVariables.mounts = InternalMethods.instance().getMounts();
        if (InternalVariables.mounts != null) {
            return InternalVariables.mounts;
        } else {
            throw new Exception();
        }
    }

    //------------------
    //# Public Methods #
    //------------------

    /**
     * This will launch the Android market looking for BusyBox
     *
     * @param activity pass in your Activity
     */
    public static void offerBusyBox(Activity activity) {
        Log.i(InternalVariables.TAG, "Launching Market for BusyBox");
        Intent i = new Intent(
                Intent.ACTION_VIEW, Uri.parse("market://details?id=stericson.busybox"));
        activity.startActivity(i);
    }

    /**
     * This will launch the Android market looking for BusyBox,
     * but will return the intent fired and starts the activity with startActivityForResult
     *
     * @param activity    pass in your Activity
     * @param requestCode pass in the request code
     * @return intent fired
     */
    public static Intent offerBusyBox(Activity activity, int requestCode) {
        Log.i(InternalVariables.TAG, "Launching Market for BusyBox");
        Intent i = new Intent(
                Intent.ACTION_VIEW, Uri.parse("market://details?id=stericson.busybox"));
        activity.startActivityForResult(i, requestCode);
        return i;
    }

    /**
     * This will launch the Android market looking for SuperUser
     *
     * @param activity pass in your Activity
     */
    public static void offerSuperUser(Activity activity) {
        Log.i(InternalVariables.TAG, "Launching Market for SuperUser");
        Intent i = new Intent(
                Intent.ACTION_VIEW, Uri.parse("market://details?id=com.noshufou.android.su"));
        activity.startActivity(i);
    }

    /**
     * This will launch the Android market looking for SuperUser,
     * but will return the intent fired and starts the activity with startActivityForResult
     *
     * @param activity    pass in your Activity
     * @param requestCode pass in the request code
     * @return intent fired
     */
    public static Intent offerSuperUser(Activity activity, int requestCode) {
        Log.i(InternalVariables.TAG, "Launching Market for SuperUser");
        Intent i = new Intent(
                Intent.ACTION_VIEW, Uri.parse("market://details?id=com.noshufou.android.su"));
        activity.startActivityForResult(i, requestCode);
        return i;
    }

    /**
     * @return <code>true</code> if su was found
     * @deprecated As of release 0.7, replaced by {@link #isRootAvailable()}
     */
    @Deprecated
    public static boolean rootAvailable() {
        return isRootAvailable();
    }

    /**
     * @return <code>true</code> if su was found.
     */
    public static boolean isRootAvailable() {
        Log.i(InternalVariables.TAG, "Checking for Root binary");
        try {
            for (String paths : getPath()) {
                File file = new File(paths + "/su");
                if (file.exists()) {
                    log("Root was found here: " + paths);
                    return true;
                }
                log("Root was NOT found here: " + paths);
            }
        } catch (Exception e) {
            Log.i(InternalVariables.TAG, "Root was not found, more information MAY be available with Debugging on.");
            if (debugMode) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * @return <code>true</code> if BusyBox was found
     * @deprecated As of release 0.7, replaced by {@link #isBusyboxAvailable()}
     */
    @Deprecated
    public static boolean busyboxAvailable() {
        return isBusyboxAvailable();
    }

    /**
     * @return <code>true</code> if BusyBox was found.
     */
    public static boolean isBusyboxAvailable() {
        Log.i(InternalVariables.TAG, "Checking for BusyBox");
        try {
            for (String paths : getPath()) {
                File file = new File(paths + "/busybox");
                if (file.exists()) {
                    log("Found BusyBox here: " + paths);
                    return true;
                }
                log("BusyBox was NOT found here: " + paths);
            }
        } catch (Exception e) {
            Log.i(InternalVariables.TAG, "BusyBox was not found, more information MAY be available with Debugging on.");
            if (debugMode) {
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }

    /**
     * @return BusyBox version is found, null if not found.
     */
    public static String getBusyBoxVersion() {
        Log.i(InternalVariables.TAG, "Getting BusyBox Version");
        try {
            InternalMethods.instance().doExec(new String[]{"busybox"});
        } catch (Exception e) {
            Log.i(InternalVariables.TAG, "BusyBox was not found, more information MAY be available with Debugging on.");
            if (debugMode) {
                e.printStackTrace();
            }
            return null;
        }
        return InternalVariables.busyboxVersion;
    }

    /**
     * @return <code>true</code> if your app has been given root access.
     * @deprecated As of release 0.7, replaced by {@link #isAccessGiven()}
     */
    @Deprecated
    public static boolean accessGiven() {
        return isAccessGiven();
    }

    /**
     * @return <code>true</code> if your app has been given root access.
     */
    public static boolean isAccessGiven() {
        Log.i(InternalVariables.TAG, "Checking for Root access");
        InternalVariables.accessGiven = false;
        InternalMethods.instance().doExec(new String[]{"id"});

        if (InternalVariables.accessGiven) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if there is enough Space on SDCard
     *
     * @param updateSize size to Check (long)
     * @return <code>true</code> if the Update will fit on SDCard,
     *         <code>false</code> if not enough space on SDCard.
     *         Will also return <code>false</code>,
     *         if the SDCard is not mounted as read/write
     * @deprecated As of release 0.7, replaced by {@link #hasEnoughSpaceOnSdCard(long)}
     */
    @Deprecated
    public static boolean EnoughSpaceOnSdCard(long updateSize) {
        return hasEnoughSpaceOnSdCard(updateSize);
    }

    /**
     * Checks if there is enough Space on SDCard
     *
     * @param updateSize size to Check (long)
     * @return <code>true</code> if the Update will fit on SDCard,
     *         <code>false</code> if not enough space on SDCard.
     *         Will also return <code>false</code>,
     *         if the SDCard is not mounted as read/write
     */
    public static boolean hasEnoughSpaceOnSdCard(long updateSize) {
        Log.i(InternalVariables.TAG, "Checking SDcard size and that it is mounted as RW");
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (updateSize < availableBlocks * blockSize);
    }

    /**
     * This will take a path, which can contain the file name as well,
     * and attempt to remount the underlying partition.
     * <p/>
     * For example, passing in the following string:
     * "/system/bin/some/directory/that/really/would/never/exist"
     * will result in /system ultimately being remounted.
     * However, keep in mind that the longer the path you supply, the more work this has to do,
     * and the slower it will run.
     *
     * @param file      file path
     * @param mountType mount type: pass in RO (Read only) or RW (Read Write)
     * @return a <code>boolean</code> which indicates whether or not the partition
     *         has been remounted as specified.
     */

    public static boolean remount(String file, String mountType) {
        //Recieved a request, get an instance of Remounter
        Remounter remounter = new Remounter();
        //send the request.
        return (remounter.remount(file, mountType));
    }

    /**
     * This method can be used to unpack a binary from the raw resources folder and store it in
     * /data/data/app.package/files/
     * This is typically useful if you provide your own C- or C++-based binary.
     * This binary can then be executed using sendShell() and its full path.
     *
     * @param context  the current activity's <code>Context</code>
     * @param sourceId resource id; typically <code>R.raw.id</code>
     * @param destName destination file name; appended to /data/data/app.package/files/
     * @param mode     chmod value for this file
     * @return a <code>boolean</code> which indicates whether or not we were
     *         able to create the new file.
     */
    public static boolean installBinary(Context context, int sourceId, String destName, String mode) {
        Installer installer;

        try {
            installer = new Installer(context);
        } catch (IOException ex) {
            if (debugMode) {
                ex.printStackTrace();
            }
            return false;
        }

        return (installer.installBinary(sourceId, destName, mode));
    }

    /**
     * This method can be used to unpack a binary from the raw resources folder and store it in
     * /data/data/app.package/files/
     * This is typically useful if you provide your own C- or C++-based binary.
     * This binary can then be executed using sendShell() and its full path.
     *
     * @param context  the current activity's <code>Context</code>
     * @param sourceId resource id; typically <code>R.raw.id</code>
     * @param binaryName destination file name; appended to /data/data/app.package/files/
     * @return a <code>boolean</code> which indicates whether or not we were
     *         able to create the new file.
     */
    public static boolean installBinary(Context context, int sourceId, String binaryName) {
        return installBinary(context, sourceId, binaryName, "700");
    }
    
    /**
     * Executes binary in a separated process. Before using this method, the binary has to be installed
     * in /data/data/app.package/files/ using the installBinary method.
     * 
     * @param context the current activity's <code>Context</code>
     * @param binaryName name of installed binary
     * @param parameter parameter to append to binary like "-vxf"
     */
    public static void runBinary(Context context, String binaryName, String parameter) {
        // executes binary as separated thread
        Runner runner = new Runner(context, binaryName, parameter);
        runner.start();
    }
    
    /**
     * This method can be used to kill a running process
     * 
     * @param processName name of process to kill
     * @return <code>true</code> if process was found and killed successfully
     */
    public static boolean killProcess(String processName) {
        Log.i(InternalVariables.TAG, "Killing process " + processName);
        InternalVariables.pid = null;
        InternalMethods.instance().doExec(new String[]{"busybox pidof " + processName});

        if (InternalVariables.pid != null) {
            InternalMethods.instance().doExec(new String[]{"busybox kill -9 " + InternalVariables.pid});

            return true;
        } else {
            return false;
        }
    }
    
    /**
     * This method can be used to to check if a process is running
     * 
     * @param processName name of process to check
     * @return <code>true</code> if process was found
     */
    public static boolean isProcessRunning(String processName) {
        Log.i(InternalVariables.TAG, "Checks if process is running: " + processName);
        InternalVariables.pid = null;
        InternalMethods.instance().doExec(new String[]{"busybox pidof " + processName});

        if (InternalVariables.pid != null) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * This restarts only Android OS without rebooting the whole device.
     * This is done by killing the main init process named zygote. Zygote is restarted
     * automatically by Android after killing it.
     */
    public static void restartAndroid() {
        Log.i(InternalVariables.TAG, "Restart Android");
        InternalMethods.instance().doExec(new String[]{"busybox killall -9 zygote"});
    }

    /**
     * Sends several shell command as su (attempts to)
     *
     * @param commands  array of commands to send to the shell
     * @param sleepTime time to sleep between each command, delay.
     * @param result    injected result object that implements the Result class
     * @return a <code>LinkedList</code> containing each line that was returned
     *         by the shell after executing or while trying to execute the given commands.
     *         You must iterate over this list, it does not allow random access,
     *         so no specifying an index of an item you want,
     *         not like you're going to know that anyways.
     * @throws InterruptedException
     * @throws IOException
     */
    public static List<String> sendShell(String[] commands, int sleepTime, Result result)
            throws IOException, InterruptedException, RootToolsException {
        if (debugMode) {
            for (String c : commands) {
                log("Shell command: " + c);
            }
        }
        return (new Executer().sendShell(commands, sleepTime, result));
    }


    /**
     * Sends several shell command as su (attempts to)
     *
     * @param commands  array of commands to send to the shell
     * @param sleepTime time to sleep between each command, delay.
     * @return a LinkedList containing each line that was returned by the shell
     *         after executing or while trying to execute the given commands.
     *         You must iterate over this list, it does not allow random access,
     *         so no specifying an index of an item you want,
     *         not like you're going to know that anyways.
     * @throws InterruptedException
     * @throws IOException
     */
    public static List<String> sendShell(String[] commands, int sleepTime)
            throws IOException, InterruptedException, RootToolsException {
        return sendShell(commands, sleepTime, null);
    }

    /**
     * Sends one shell command as su (attempts to)
     *
     * @param command command to send to the shell
     * @param result  injected result object that implements the Result class
     * @return a <code>LinkedList</code> containing each line that was returned
     *         by the shell after executing or while trying to execute the given commands.
     *         You must iterate over this list, it does not allow random access,
     *         so no specifying an index of an item you want,
     *         not like you're going to know that anyways.
     * @throws InterruptedException
     * @throws IOException
     * @throws RootToolsException
     */
    public static List<String> sendShell(String command, Result result)
            throws IOException, InterruptedException, RootToolsException {
        return sendShell(new String[]{command}, 0, result);
    }

    /**
     * Sends one shell command as su (attempts to)
     *
     * @param command command to send to the shell
     * @return a LinkedList containing each line that was returned by the shell
     *         after executing or while trying to execute the given commands.
     *         You must iterate over this list, it does not allow random access,
     *         so no specifying an index of an item you want,
     *         not like you're going to know that anyways.
     * @throws InterruptedException
     * @throws IOException
     */
    public static List<String> sendShell(String command)
            throws IOException, InterruptedException, RootToolsException {
        return sendShell(command, null);
    }

    /**
     * Get the space for a desired partition.
     *
     * @param path The partition to find the space for.
     * @return the amount if space found within the desired partition.
     *         If the space was not found then the value is -1
     */
    public static long getSpace(String path) {
        InternalVariables.getSpaceFor = path;
        boolean found = false;
        String[] commands = {"df " + path};
        InternalMethods.instance().doExec(commands);

        RootTools.log("Looking for Space");

        if (InternalVariables.space != null) {
            RootTools.log("First Method");

            for (String spaceSearch : InternalVariables.space) {

                RootTools.log(spaceSearch);

                if (found) {
                    return InternalMethods.instance().getConvertedSpace(spaceSearch);
                } else if (spaceSearch.equals("used,")) {
                    found = true;
                }
            }

            //Try this way
            int count = 0,
                    targetCount = 3;

            RootTools.log("Second Method");

            if (!InternalVariables.space[0].startsWith(path)) {
                targetCount = 2;
            }

            for (String spaceSearch : InternalVariables.space) {

                RootTools.log(spaceSearch);
                if (spaceSearch.length() > 0) {
                    RootTools.log(spaceSearch + ("Valid"));
                    if (count == targetCount) {
                        return InternalMethods.instance().getConvertedSpace(spaceSearch);
                    }
                    count++;
                }
            }
        }
        RootTools.log("Returning -1, space could not be determined.");
        return -1;
    }

    /**
     * This method allows you to output debug messages only when debugging is on.
     * This will allow you to add a debug option to your app, which by default can be
     * left off for performance. However, when you need debugging information, a simple
     * switch can enable it and provide you with detailed logging.
     * <p/>
     * This method handles whether or not to log the information you pass it depending
     * whether or not RootTools.debugMode is on. So you can use this and not have to
     * worry about handling it yourself.
     *
     * @param TAG Optional parameter to define the tag that the Log will use.
     * @param msg The message to output.
     */
    public static void log(String msg) {
        log(null, msg);
    }

    public static void log(String TAG, String msg) {
        if (debugMode) {
            if (TAG != null) {
                Log.d(TAG, msg);
            } else {
                Log.d(InternalVariables.TAG, msg);
            }
        }
    }

    public static abstract class Result implements IResult {
        private Process process = null;
        private Serializable data = null;
        private int error = 0;

        public abstract void process(String line) throws Exception;

        public abstract void onFailure(Exception ex);

        public abstract void onComplete(int diag);

        public Result setProcess(Process process) {
            this.process = process;
            return this;
        }

        public Process getProcess() {
            return process;
        }

        public Result setData(Serializable data) {
            this.data = data;
            return this;
        }

        public Serializable getData() {
            return data;
        }

        public Result setError(int error) {
            this.error = error;
            return this;
        }

        public int getError() {
            return error;
        }
    }
}
