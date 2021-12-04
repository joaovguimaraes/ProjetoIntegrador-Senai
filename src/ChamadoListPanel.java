import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChamadoListPanel extends JPanel {
    private AppFrame frame;

    private JButton btnCriar;
    private JButton btnDistancia;
    private JButton btnEditar;
    private JButton btnRemover;
    private JButton btnVoltar;
    private JTable tabela;
    private ChamadoTableModel tableModel;

    public ChamadoListPanel(AppFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10,10));
        setBackground(Color.gray);

        criarCabecalho();
        criarTabelaPanel();
        criarComandosPanel();
    }

    public void criarCabecalho() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(1280,30));
        panel.setBackground(Color.black);
        JLabel titulo = new JLabel ("Chamados");
        titulo.setForeground(Color.white);;
        panel.add(titulo);
        add(panel, BorderLayout.NORTH);
    }

    private void criarComandosPanel() {
        JPanel panel = new JPanel();
        FlowLayout layout = (FlowLayout) panel.getLayout();
        layout.setAlignment(FlowLayout.RIGHT);

        criarBtnVoltar();
        panel.add(btnVoltar);

        criarBtnCriar();
        panel.add(btnCriar);

        criarBtnEditar();
        panel.add(btnEditar);

        criarBtnRemover();
        panel.add(btnRemover);

        criarBtnDistancia();
        panel.add(btnDistancia);

        add(panel, BorderLayout.NORTH);

        desabilitarBtns();
    }

    private void criarBtnVoltar(){
        btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.mostrarInicialPanel();
            }
        });
    }

    private void criarBtnDistancia(){
        btnDistancia = new JButton("Adicionar Distancia");
        btnDistancia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.mostrarChamadoDistanciaFormPanel(tableModel.getChamado(tabela.getSelectedRow()));
            }
        });
    }

    private void criarBtnCriar() {
        btnCriar = new JButton("Criar");
        btnCriar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.mostrarChamadoFormPanel(null);
            }
        });
    }

    private void criarBtnEditar() {
        btnEditar = new JButton("Editar");
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.mostrarChamadoFormPanel(tableModel.getChamado(tabela.getSelectedRow()));
            }
        });
    }

    private void criarBtnRemover() {
        btnRemover = new JButton("Remover");
        btnRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Chamado chamado = tableModel.getChamado(tabela.getSelectedRow());
                int resposta = JOptionPane.showConfirmDialog(ChamadoListPanel.this, "Deseja realmente remover?", "Todo App", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (resposta == JOptionPane.YES_OPTION) {
                    ChamadoStorage.remover(chamado);
                    recarregar();
                }
            }
        });
    }

    private void criarTabelaPanel() {
        JPanel panel = new JPanel();

        tableModel = new ChamadoTableModel(ChamadoStorage.listar());
        tabela = new JTable(tableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (tabela.getSelectedRow() >= 0) {
                        habilitarBtns();
                    } else {
                        desabilitarBtns();
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(tabela);
        panel.add(scrollPane);

        add(panel, BorderLayout.CENTER);
    }

    private void habilitarBtns() {
        btnEditar.setEnabled(true);
        btnRemover.setEnabled(true);
        btnDistancia.setEnabled(true);
    }


    private void desabilitarBtns() {
        btnEditar.setEnabled(false);
        btnRemover.setEnabled(false);
        btnDistancia.setEnabled(false);
    }

    public void recarregar() {
        tableModel.carregar(ChamadoStorage.listar());
    }
}

