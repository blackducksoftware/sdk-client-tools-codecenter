/*
 * Black Duck Software Suite SDK Copyright (C) 2015 Black Duck Software, Inc.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package com.blackducksoftware.sdk.codecenter.client.examples;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.ParserProperties;

import com.blackducksoftware.sdk.codecenter.client.examples.util.BDCmdLineParser;
import com.blackducksoftware.sdk.codecenter.client.examples.util.BDOptionSorter;
import com.blackducksoftware.sdk.codecenter.client.util.CodeCenterServerProxy;

/**
 * A Black Duck Code Center SDK sample program.
 *
 * <p>
 * Subclasses should define command-line options and arguments as fields annotated with {@link Option @Option} and
 * {@link Argument @Argument}, respectively. Argument indices should start at 3, as this class defines server URI,
 * username, and password arguments at indices 0-2. This class also defines a default timeout option for communicating
 * with the server.
 * </p>
 */
public abstract class BDCodeCenterSample {

    /** The timeout in seconds when communicating with the server. */
    @Option(name = "--timeout", hidden = true, usage = "the timeout in seconds when communicating with the server")
    protected long defaultTimeout = 120;

    /** The base URI for the server. */
    @Argument(index = 0, required = true, metaVar = "serverUri", usage = "the base URI for the server (ex: http://codecenter.example.com:80/)")
    protected String serverUri;

    /** The username to log in with. */
    @Argument(index = 1, required = true, metaVar = "loginUsername", usage = "the username to log in with")
    protected String username;

    /** The password to log in with. */
    @Argument(index = 2, required = true, metaVar = "loginPassword", usage = "the password to log in with")
    protected String password;

    /**
     * The arguments provided to run this sample with.
     *
     * <p>
     * Argument parsing could be performed in the constructor rather than the {@link #run()} method, eliminating the
     * need to store the arguments in this field. However, this abstract base class' constructor executes and finishes
     * before any subclasses can initialize fields. This includes any options/arguments defined as annotated fields in
     * subclasses, which would result in their default values being shown in the usage details as the uninitialized
     * value for their type.
     * </p>
     */
    private final String[] args;

    /** The {@link CmdLineParser} properties to parse the arguments with */
    private final CmdLineParser parser;

    /**
     * Constructs an instance of this Black Duck Code Center SDK sample program with the provided arguments.
     *
     * @param args
     *            The arguments to run this sample with; should include, in order: the server URI, username, password,
     *            and sample-specific parameters
     */
    public BDCodeCenterSample(String[] args) {
        this(args, ParserProperties.defaults());
    }

    /**
     * Constructs an instance of this Black Duck Code Center SDK sample program with the provided arguments and parser
     * properties.
     *
     * @param args
     *            The arguments to run this sample with; should include, in order: the server URI, username, password,
     *            and sample-specific parameters
     * @param parserProperties
     *            The {@link CmdLineParser} properties to parse the arguments with
     * @see ParserProperties
     */
    public BDCodeCenterSample(String[] args, ParserProperties parserProperties) {
        this.args = args;
        parser = new BDCmdLineParser(this, this.getClass().getSimpleName(), parserProperties.withOptionSorter(new BDOptionSorter(parserProperties)));
    }

    /**
     * Parses/validates the arguments initially provided and runs this sample.
     *
     * <p>
     * Invokes the {@link #preprocessArgs()} and {@link #run(CodeCenterServerProxy)} methods, in order. If an exception
     * is thrown, its message is printed to standard error and execution is terminated. If the exception was thrown
     * during argument validation, the usage is printed as well.
     * </p>
     */
    protected void run() throws Error {
        try {
            parser.parseArgument(args);
        } catch (Exception e) {
            err(e, true);
            return;
        }

        try {
            preprocessArgs();
        } catch (Exception e) {
            err(e, true);
            return;
        }

        try (CodeCenterServerProxy proxy = new CodeCenterServerProxy(serverUri, username, password, defaultTimeout * 1000)) {
            run(proxy);
        } catch (CmdLineException e) {
            err(e, true);
            return;
        } catch (Exception e) {
            err(e, false);
            return;
        }
    }

    /**
     * Preprocesses/validates the arguments initially provided to this sample.
     *
     * <p>
     * Override this method to preprocess/validate arguments. No connection to the server should be made yet, so limit
     * processing to the argument values only.
     * </p>
     *
     * @throws Exception
     *             If argument validation does not succeed
     * @see #run()
     */
    protected void preprocessArgs() throws Exception {}

    /**
     * Runs the sample.
     *
     * @param proxy
     *            A proxy to the Code Center server
     * @throws Exception
     *             If the sample does not complete successfully
     * @see #run()
     * @see CodeCenterServerProxy
     */
    protected abstract void run(CodeCenterServerProxy proxy) throws Exception;

    /**
     * Prints an exception's message to standard error and terminates execution with a nonzero status code.
     *
     * @param e
     *            The exception
     * @param printUsage
     *            If {@code true}, {@linkplain #printUsage() print the usage details} for this sample as well
     * @throws Throwable
     *             If the exception is one of the following types, then it was almost certainly caused by a bug and the
     *             exception will be rethrown for debugging purposes:
     *             <ul>
     *             <li>{@link ClassCastException}</li>
     *             <li>{@link IndexOutOfBoundsException}</li>
     *             <li>{@link NullPointerException}</li>
     *             <li>{@link Error}</li>
     *             </ul>
     */
    private void err(Throwable e, boolean printUsage) {
        System.err.println();
        System.err.println(e.getMessage());

        if (printUsage) {
            System.err.println();
            parser.printSingleLineUsage(System.err);
            System.err.println();
            parser.printUsage(System.err);
        }

        try {
            throw e;
        } catch (ClassCastException | IndexOutOfBoundsException | NullPointerException | Error e_) {
            throw e_;
        } catch (@SuppressWarnings("unused") Throwable e_) {}

        System.exit(-1);
    }

}
