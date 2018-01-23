package com.example.rajatha.androidsoapexample;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.kobjects.util.Strings;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Strings>>{

    private static String NAMESPACE = "http://www.webserviceX.NET";//NAMESPACE is the URL of the webservice
    private static String METHOD_NAME = "GetCitiesByCountry";
    private static String SOAP_ACTION = NAMESPACE + "/" +METHOD_NAME;
    private static String URL = "http://www.webservicex.net/globalweather.asmx?WSDL";

    //Loader arguments

    private static int LOADER_ID =1;

    TextView mtext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtext = (TextView)findViewById(R.id.resultbox) ;
        mtext.setText("Hello World Again!");

       getLoaderManager().initLoader(LOADER_ID,null,this);



    }


    @Override
    public Loader<List<Strings>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Strings>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public List<Strings> loadInBackground() {

                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
                request.addProperty("CountryName","Germany");
                //envelop is basically the output
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                envelope.dotNet=true;
                envelope.setOutputSoapObject(request);

                //Make the call
                HttpTransportSE httpTransport = new HttpTransportSE(URL);

                List<Strings> mCitiyList= null;

                try{
                    httpTransport.call(SOAP_ACTION,envelope);
                   // mCitiyList=extractDataFromXmlResponse(envelope);
                } catch (HttpResponseException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("Soap",envelope.bodyIn.toString());

                return mCitiyList;
            }


        };
    }


   /* private List<Strings> extractDataFromXmlResponse(SoapSerializationEnvelope envelope) throws ParserConfigurationException, IOException, SAXException {

        List<Strings> citiesList = new ArrayList<>();
        DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuildFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new InputSource(new StringReader(envelope.getResponse().toString())));
        NodeList nodeList = doc.getElementsByTagName("Table");

        for(int i =0; i<nodeList.getLength()-1;i++){
            Node node = nodeList.item(i);
            if(node.getNodeType()== Node.ELEMENT_NODE){
               Element  element = (Element)node;
                citiesList.add(element.getElementsByTagName("City").item(0).getTextContent());
            }
        }
        return citiesList;
    }*/

    @Override
    public void onLoadFinished(Loader<List<Strings>> loader, List<Strings> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<Strings>> loader) {

    }
}
