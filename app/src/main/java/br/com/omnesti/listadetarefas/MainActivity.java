package br.com.omnesti.listadetarefas;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText edtTarefa;
    private Button btnCadastrar;
    private ListView listaTarefas;
    private SQLiteDatabase bancodedados;
    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<String> itens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            edtTarefa = (EditText) findViewById(R.id.edtTarefa);
            btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
            listaTarefas = (ListView) findViewById(R.id.listTarefas);

            bancodedados = openOrCreateDatabase("ListaTarefasDB", MODE_PRIVATE, null);

            bancodedados.execSQL("CREATE TABLE IF NOT EXISTS tarefas (id INTEGER PRIMARY KEY AUTOINCREMENT, tarefa VARCHAR)");

            btnCadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tarefa = edtTarefa.getText().toString();
                    if (edtTarefa.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Escreva uma tarefa", Toast.LENGTH_SHORT).show();
                    }else{
                        salvarTarefa(tarefa);
                        edtTarefa.setText(" ");
                        Toast.makeText(getApplicationContext(),"Tarefa Salva", Toast.LENGTH_SHORT).show();
                        recuperarTarefas();
                    }
                }
            });

            recuperarTarefas();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void salvarTarefa(String texto){
        try {
            bancodedados.execSQL("INSERT INTO tarefas (tarefa) VALUES ('" + texto + "')");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void recuperarTarefas()
    {
        try{
            Cursor cursor = bancodedados.rawQuery("SELECT * FROM TAREFAS ORDER BY  id DESC", null);

            int indiceColId = cursor.getColumnIndex("id");
            int indiceColTarefa = cursor.getColumnIndex("tarefa");

            itens = new ArrayList<String>();

            itensAdaptador = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2,android.R.id.text1, itens);

            listaTarefas.setAdapter(itensAdaptador);

            cursor.moveToFirst();
            while (cursor != null){
                itens.add(cursor.getString(indiceColTarefa));
                cursor.moveToNext();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
