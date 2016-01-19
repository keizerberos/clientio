package com.example.ioandroid;

import android.support.annotation.MainThread;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends ActionBarActivity {
	private Socket socket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			//socket = IO.socket("http://ioserver-antrax.rhcloud.com:8000");
			socket = IO.socket("http://192.168.0.92:8000");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
				socket.emit("message", "hi");
			}

		}).on("message", new Emitter.Listener() {

			@Override
			public void call(final Object... args) {
				System.out.println("recibido");
				Log.v("debug", "recibido");
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String message = (String) args[0];
						TextView texto = (TextView) findViewById(R.id.texto);
						texto.setText( message);
/*
						JSONObject data = (JSONObject) args[0];
						String message;
						try {
							message = data.getString("message");
						} catch (JSONException e) {
							return;
						}
						TextView texto = (TextView) findViewById(R.id.texto);
						texto.setText(message);*/
					}
				});
			}

			// @Override
			// public void call(final Object... args) {
			// MainActivity.this.runOnUiThread(new Runnable() {
			// @Override
			// public void run() {
			// JSONObject data = (JSONObject) args[0];
			// String username;
			// String message;
			// try {
			// username = data.getString("username");
			// message = data.getString("message");
			// } catch (JSONException e) {
			// return;
			// }
			//
			// addMessage(username, message);
			// }
			// });
			// }

		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

			@Override
			public void call(Object... args) {
			}

		});
		socket.connect();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		socket.disconnect();
		super.onDestroy();
	}
}
