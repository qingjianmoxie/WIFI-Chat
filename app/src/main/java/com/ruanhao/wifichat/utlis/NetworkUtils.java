package com.ruanhao.wifichat.utlis;

import android.util.Log;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class containing utility methods for network operations.
 *
 * @author Christian Ihle
 */
public class NetworkUtils {

	/** The logger. */
	private static final Logger LOG = Logger.getLogger(NetworkUtils.class.getName());

	/**
	 * Checks if the network interface is up, and usable.
	 *
	 * <p>
	 * A network interface is usable when it:
	 * </p>
	 *
	 * <ul>
	 * <li>Is up.</li>
	 * <li>Supports multicast.</li>
	 * <li>Is not a loopback device, like localhost.</li>
	 * <li>Is not a point to point device, like a modem.</li>
	 * <li>Is not virtual, like <code>eth0:1</code>.</li>
	 * <li>Is not a virtual machine network interface (vmnet).</li>
	 * <li>Has an IPv4 address.</li>
	 * </ul>
	 *
	 * @param netif
	 *            The network interface to check.
	 * @return True if the network interface is usable.
	 */
	public boolean isUsable(final NetworkInterface netif) {
		if (netif == null) {
			return false;
		}

		try {
			return netif.isUp() && !netif.isLoopback() && !netif.isPointToPoint() && !netif.isVirtual()
					&& netif.supportsMulticast() && !netif.getName().toLowerCase().contains("vmnet")
					&& !netif.getDisplayName().toLowerCase().contains("vmnet") && hasIPv4Address(netif);
		}

		catch (final SocketException e) {
			LOG.log(Level.WARNING, e.toString());
			return false;
		}
	}

	/**
	 * Checks if the network interface has an IPv4-address.
	 *
	 * @param netif
	 *            The network interface to check.
	 * @return If an IPv4-address was found or not.
	 */
	public boolean hasIPv4Address(final NetworkInterface netif) {
		if (netif == null) {
			return false;
		}

		final Enumeration<InetAddress> inetAddresses = netif.getInetAddresses();

		while (inetAddresses.hasMoreElements()) {
			final InetAddress inetAddress = inetAddresses.nextElement();
			if (inetAddress instanceof Inet4Address) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Constructs a string with the information found on a
	 * {@link NetworkInterface}.
	 *
	 * @param netif
	 *            The network interface to check.
	 * @return A string with information.
	 */
	public String getNetworkInterfaceInfo(final NetworkInterface netif) {
		if (netif == null) {
			return "Invalid network interface.";
		}

		try {
			return "Interface name: " + netif.getDisplayName() + "\n" + "Device: " + netif.getName() + "\n"
					+ "Is loopback: " + netif.isLoopback() + "\n" + "Is up: " + netif.isUp() + "\n" + "Is p2p: "
					+ netif.isPointToPoint() + "\n" + "Is virtual: " + netif.isVirtual() + "\n" + "Supports multicast: "
					+ netif.supportsMulticast() + "\n" + "MAC address: " + getMacAddress(netif) + "\n"
					+ "IP addresses: " + getIPv4Addresses(netif);
		}

		catch (final SocketException e) {
			LOG.log(Level.WARNING, e.toString());
			return "Failed to get network interface information.";
		}
	}

	/**
	 * Returns a list of the IPv4-addresses on the network interface in string
	 * format.
	 *
	 * @param netif
	 *            The network interface to get the IPv4-addresses from.
	 * @return All the IPv4-addresses on the network interface.
	 */
	public String getIPv4Addresses(final NetworkInterface netif) {
		if (netif == null) {
			return "";
		}

		String ipAddress = "";
		final Enumeration<InetAddress> inetAddresses = netif.getInetAddresses();

		while (inetAddresses.hasMoreElements()) {
			final InetAddress inetAddress = inetAddresses.nextElement();
			if (inetAddress instanceof Inet4Address) {
				ipAddress += inetAddress.getHostAddress() + " ";
			}
		}

		return ipAddress;
	}
	//得到本机IP地址
	public static String getLocalIpAddress(){
		try{
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); 
			while(en.hasMoreElements()){
				NetworkInterface nif = en.nextElement();
				Enumeration<InetAddress> enumIpAddr = nif.getInetAddresses();
				while(enumIpAddr.hasMoreElements()){
					InetAddress mInetAddress = enumIpAddr.nextElement();
//					if(!mInetAddress.isLoopbackAddress()/**&& InetAddressUtils.isIPv4Address(mInetAddress.getHostAddress())**/){
					if(!mInetAddress.isLoopbackAddress()&&!mInetAddress.isLinkLocalAddress()){
						return mInetAddress.getHostAddress().toString();
					}
				}
			}
		}catch(SocketException ex){
			Log.e("MyFeiGeActivity", "获取本地IP地址失败");
		}
		
		return null;
	}
	//得到本机IP地址
	public static InetAddress getLocalInetAddress(){
		try{
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); 
			while(en.hasMoreElements()){
				NetworkInterface nif = en.nextElement();
				Enumeration<InetAddress> enumIpAddr = nif.getInetAddresses();
				while(enumIpAddr.hasMoreElements()){
					InetAddress mInetAddress = enumIpAddr.nextElement();
					if(!mInetAddress.isLoopbackAddress() /**&& InetAddressUtils.isIPv4Address(mInetAddress.getHostAddress())**/){
						return mInetAddress;
					}
				}
			}
		}catch(SocketException ex){
			Log.e("MyFeiGeActivity", "获取本地IP地址失败");
		}
		
		return null;
	}
	/**
	 * Returns the MAC-address of the network interface, in hex format.
	 *
	 * @param netif
	 *            The network interface to get the MAC-address of.
	 * @return The MAC-address in hex, as a string.
	 */
	public String getMacAddress(final NetworkInterface netif) {
		if (netif == null) {
			return "";
		}

		String macAddress = "";
		byte[] address = null;

		try {
			address = netif.getHardwareAddress();
		}

		catch (final SocketException e) {
			LOG.log(Level.WARNING, e.toString());
		}

		if (address != null) {
			// Convert byte array to hex format
			for (int i = 0; i < address.length; i++) {
				macAddress += String.format("%02x", address[i]);
				if (i != address.length - 1) {
					macAddress += "-";
				}
			}
		}

		return macAddress.toUpperCase();
	}

	/**
	 * Fetches all the network interfaces again, and returns the one which is
	 * the same as the original network interface.
	 *
	 * <p>
	 * This is useful to make sure the network interface information is up to
	 * date, like the current ip address.
	 * </p>
	 *
	 * @param origNetIf
	 *            The original network interface to compare with.
	 * @return An updated version of the same network interface, or
	 *         <code>null</code> if not found.
	 */
	public NetworkInterface getUpdatedNetworkInterface(final NetworkInterface origNetIf) {
		if (origNetIf == null) {
			return null;
		}

		final Enumeration<NetworkInterface> networkInterfaces = getNetworkInterfaces();

		if (networkInterfaces == null) {
			return null;
		}

		while (networkInterfaces.hasMoreElements()) {
			final NetworkInterface netif = networkInterfaces.nextElement();
			if (sameNetworkInterface(origNetIf, netif)) {
				return netif;
			}
		}

		return null;
	}

	/**
	 * Compares 2 network interfaces. The only way the 2 network interfaces can
	 * be considered the same is if they have the exact same name.
	 *
	 * <p>
	 * If any of the network interfaces are <code>null</code> then they are not
	 * considered the same.
	 * </p>
	 *
	 * @param netIf1
	 *            The first network interface.
	 * @param netIf2
	 *            The second network interface.
	 * @return If they are the same or not.
	 */
	public boolean sameNetworkInterface(final NetworkInterface netIf1, final NetworkInterface netIf2) {
		if (netIf1 == null || netIf2 == null) {
			return false;
		}

		return netIf1.getName().equals(netIf2.getName());
	}

	/**
	 * Iterates through a list of available network interfaces, and returns the
	 * first that is usable. Returns <code>null</code> if no usable interface is
	 * found.
	 *
	 * @return The first usable network interface, or <code>null</code>.
	 * @see #isUsable(NetworkInterface)
	 */
	public NetworkInterface findFirstUsableNetworkInterface() {
		final Enumeration<NetworkInterface> networkInterfaces = getNetworkInterfaces();

		if (networkInterfaces == null) {
			return null;
		}

		while (networkInterfaces.hasMoreElements()) {
			final NetworkInterface netif = networkInterfaces.nextElement();
			if (isUsable(netif)) {
				return netif;
			}
		}

		return null;
	}

	/**
	 * Gets all the available network interfaces. Returns <code>null</code> if
	 * the operation fails, or no interfaces are available.
	 *
	 * @return All network interfaces, or <code>null</code>.
	 */
	public Enumeration<NetworkInterface> getNetworkInterfaces() {
		Enumeration<NetworkInterface> networkInterfaces = null;

		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		}

		catch (final SocketException e) {
			LOG.log(Level.WARNING, e.toString());
		}

		return networkInterfaces;
	}

	/**
	 * Iterates through a list of available network interfaces, and returns all
	 * that are usable.
	 *
	 * @return All the usable network interfaces.
	 * @see #isUsable(NetworkInterface)
	 */
//	public List<NetworkInterfaceInfo> getUsableNetworkInterfaces() {
//		final List<NetworkInterfaceInfo> usableNetworkInterfaces = new ArrayList<NetworkInterfaceInfo>();
//		final Enumeration<NetworkInterface> allNetworkInterfaces = getNetworkInterfaces();
//
//		if (allNetworkInterfaces == null) {
//			return usableNetworkInterfaces;
//		}
//
//		while (allNetworkInterfaces.hasMoreElements()) {
//			final NetworkInterface netif = allNetworkInterfaces.nextElement();
//
//			if (isUsable(netif)) {
//				usableNetworkInterfaces.add(new NetworkInterfaceInfo(netif));
//			}
//		}
//
//		return usableNetworkInterfaces;
//	}

	/**
	 * Gets the name of the localhost.
	 *
	 * @return The host name, or <code>null</code> if the host name cannot be
	 *         determined.
	 */
	public String getLocalHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		}

		catch (final UnknownHostException e) {
			LOG.log(Level.WARNING, e.toString());
		}

		return null;
	}

	/**
	 * Gets the network interfaces with the requested name. Returns
	 * <code>null</code> if no interface is found with that name.
	 *
	 * @param name
	 *            Name of the network interface to return.
	 * @return The requested network interface, or <code>null</code>.
	 */
	public NetworkInterface getNetworkInterfaceByName(final String name) {
		if (name == null) {
			return null;
		}

		try {
			return NetworkInterface.getByName(name);
		}

		catch (final SocketException e) {
			LOG.log(Level.WARNING, e.toString());
			return null;
		}
	}
}